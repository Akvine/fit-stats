package ru.akvine.fitstats.config.security.ssl;

import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.core.io.Resource;

import javax.net.ssl.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyStore;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Arrays;

@Slf4j
public class SecureConnectionsManager implements SSLContextProvider, HostnameVerifierProvider {
    private static final HostnameVerifier HOSTNAME_VERIFIER_NO_VERIFY = (s, sslSession) -> true;

    private final Resource keyStoreResource;
    private final String password;
    private final boolean doVerifyHostname;
    private String clientAlias;
    private KeyStore keyStore;
    private SSLContext sslContext;

    public SecureConnectionsManager(Resource keyStoreResource, String password, boolean doVerifyHostname, @Nullable String clientAlias) {
        this.keyStoreResource = keyStoreResource;
        this.password = password;
        this.doVerifyHostname = doVerifyHostname;
        this.clientAlias = clientAlias;

        load();
    }

    public SecureConnectionsManager(Resource keyStoreResource, String password, boolean doVerifyHostname) {
        this.keyStoreResource = keyStoreResource;
        this.password = password;
        this.doVerifyHostname = doVerifyHostname;

        load();
    }

    @Override
    public @Nullable HostnameVerifier getHostnameVerifier() {
        if (isDoVerifyHostname()) {
            return null;
        }

        return HOSTNAME_VERIFIER_NO_VERIFY;
    }

    @Override
    public SSLContext getSslContext() {
        SSLContext sslContext = this.sslContext;
        Preconditions.checkState(sslContext != null, "sslContext not initialized");
        return sslContext;
    }

    @NotNull
    private static X509TrustManager getX509TrustManager(TrustManager... trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
            logger.debug("Skipping " + trustManager);
        }
        throw new RuntimeException("Not found X509TrustManager in array " + Arrays.toString(trustManagers));
    }

    @NotNull
    private static X509KeyManager getX509KeyManager(KeyManager... keyManagers) {
        for (KeyManager keyManager : keyManagers) {
            if (keyManager instanceof X509KeyManager) {
                return (X509KeyManager) keyManager;
            }
            logger.debug("Skipping " + keyManager);
        }
        throw new RuntimeException("Not found X509KeyManager in array " + Arrays.toString(keyManagers));
    }

    class DelegatingTrustManager implements X509TrustManager {

        private final X509TrustManager trustManagerDefault;
        private final X509TrustManager trustManager;


        public DelegatingTrustManager(TrustManager[] trustManagersDefault, TrustManager[] trustManagers) {
            this.trustManagerDefault = getX509TrustManager(trustManagersDefault);
            this.trustManager = getX509TrustManager(trustManagers);
        }

        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            logger.debug("Check client trusted");
            try {
                trustManager.checkClientTrusted(chain, authType);
            } catch (CertificateException exception) {
                try {
                    // default CA check
                    trustManagerDefault.checkClientTrusted(chain, authType);
                } catch (CertificateException exception1) {
                    logger.error("Client trust failed", exception1);
                }
            }
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
            logger.debug("Check server trusted");
            try {
                trustManager.checkServerTrusted(chain, authType);
            } catch (CertificateException exception) {
                try {
                    // default CA check
                    trustManagerDefault.checkServerTrusted(chain, authType);
                } catch (CertificateException exception1) {
                    logger.debug("Server check failed: {}", exception1.getMessage());
                }
            }
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            X509Certificate[] certs = trustManager.getAcceptedIssuers();
            X509Certificate[] result = ArrayUtils.addAll(
                    certs,
                    trustManagerDefault.getAcceptedIssuers()
            );
            // log only explicit jks certs
            for (X509Certificate cert : certs) {
                logger.trace("Accepted issuer: " + cert.getSubjectDN());
            }
            return result;
        }
    }

    class DelegatingKeyManager implements X509KeyManager {
        private final X509KeyManager defaultManager;
        private final String clientAlias;

        DelegatingKeyManager(KeyManager[] keyManagers, @Nullable String clientAlias) {
            this.defaultManager = getX509KeyManager(keyManagers);
            this.clientAlias = clientAlias;
        }


        @Override
        public String[] getClientAliases(String s, Principal[] principals) {
            logger.debug("getClientAliases, s=" + s);
            return defaultManager.getClientAliases(s, principals);
        }

        @Override
        public String chooseClientAlias(String[] strings, Principal[] principals, Socket socket) {
            logger.trace("chooseClientAlias for principals, see below");
            for (Principal pr : principals) {
                logger.trace("Principal: " + pr.getName());
            }

            String chosenAlias;
            if (StringUtils.isNotBlank(clientAlias)) {
                chosenAlias = clientAlias;
            } else {
                chosenAlias = defaultManager.chooseClientAlias(strings, principals, socket);
            }

            logger.trace("chooseClientAlias result: " + chosenAlias);
            return chosenAlias;
        }

        @Override
        public String[] getServerAliases(String s, Principal[] principals) {
            logger.debug("getServerAliases");
            return defaultManager.getServerAliases(s, principals);
        }

        @Override
        public String chooseServerAlias(String s, Principal[] principals, Socket socket) {
            logger.debug("chooseServerAlias");
            return defaultManager.chooseServerAlias(s, principals, socket);
        }

        @Override
        public X509Certificate[] getCertificateChain(String s) {
            logger.trace("getCertificateChain for " + s);
            return defaultManager.getCertificateChain(s);
        }

        @Override
        public PrivateKey getPrivateKey(String s) {
            logger.trace("Get certificate chain for " + s);
            return defaultManager.getPrivateKey(s);
        }
    }

    private boolean isDoVerifyHostname() {
        return doVerifyHostname;
    }

    private void load() {
        InputStream stream = null;
        try {
            stream = keyStoreResource.getInputStream();
        } catch (IOException exception) {
            logger.error("Cannot load input stream from given resource", exception);
        }
        if (stream == null) {
            logger.error("Invalid certificate resource given");
        }

        try {
            keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(stream, password.toCharArray());
            logger.debug("KeyStore has " + keyStore.size() + " elements.");

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, password.toCharArray());

            TrustManagerFactory trustManagerFactoryDefault = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactoryDefault.init((KeyStore) null);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            TrustManager[] trustManagers = new TrustManager[]{
                    new DelegatingTrustManager(
                            trustManagerFactoryDefault.getTrustManagers(),
                            trustManagerFactory.getTrustManagers()
                    )
            };
            KeyManager[] keyManagers = new KeyManager[]{
                    new DelegatingKeyManager(kmf.getKeyManagers(), clientAlias)
            };

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(keyManagers, trustManagers, new SecureRandom());

            SSLContext.setDefault(sslContext);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

            if (!doVerifyHostname) {
                HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER_NO_VERIFY);
            }
            logger.info("Certificates installed successfully");
        } catch (Exception exception) {
            logger.error("Cannot init keyStore", exception);
        }
    }
}
