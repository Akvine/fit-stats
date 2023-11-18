CREATE TABLE IF NOT EXISTS CLIENT_ENTITY
(
    ID BIGINT NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED BOOLEAN NOT NULL,
    DELETED_DATE TIMESTAMP,
    EMAIL VARCHAR2(255) NOT NULL,
    FIRST_NAME VARCHAR2(255) NOT NULL,
    HASH VARCHAR2(255) NOT NULL,
    SECOND_NAME VARCHAR2(255) NOT NULL,
    THIRD_NAME VARCHAR2(255),
    UUID VARCHAR2(255) NOT NULL,
    CONSTRAINT CLIENT_PKEY PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS BIOMETRIC_ENTITY
(
    ID BIGINT NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP,
    AGE INTEGER NOT NULL,
    GENDER VARCHAR2(255),
    HEIGHT VARCHAR2(255) NOT NULL,
    HEIGHT_MEASUREMENT VARCHAR2(255) NOT NULL,
    PHYSICAL_ACTIVITY VARCHAR2(255) NOT NULL,
    WEIGHT VARCHAR2(255) NOT NULL,
    WEIGHT_MEASUREMENT VARCHAR2(255) NOT NULL,
    client_id bigint,
    CONSTRAINT BIOMETRIC_PKEY PRIMARY KEY (ID),
    CONSTRAINT BIOMETRIC_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);

CREATE TABLE IF NOT EXISTS PRODUCT_ENTITY
(
    ID BIGINT NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED BOOLEAN NOT NULL,
    DELETED_DATE TIMESTAMP,
    CALORIES DOUBLE PRECISION NOT NULL,
    CARBOHYDRATES DOUBLE PRECISION NOT NULL,
    FATS DOUBLE PRECISION NOT NULL,
    MEASUREMENT VARCHAR2(64),
    PRODUCER VARCHAR2(255) NOT NULL,
    PROTEINS DOUBLE PRECISION NOT NULL,
    TITLE VARCHAR2(255) NOT NULL,
    UUID VARCHAR2(255) NOT NULL,
    VOLUME DOUBLE PRECISION NOT NULL,
    CONSTRAINT PRODUCT_PKEY PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS DIET_RECORD_ENTITY
(
    ID BIGINT NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP,
    CALORIES DOUBLE PRECISION NOT NULL,
    CARBOHYDRATES DOUBLE PRECISION NOT NULL,
    DATE DATE NOT NULL,
    FATS DOUBLE PRECISION NOT NULL,
    PROTEINS DOUBLE PRECISION NOT NULL,
    TIME TIME,
    UUID VARCHAR2(255) NOT NULL,
    VOLUME DOUBLE PRECISION NOT NULL,
    CLIENT_ID BIGINT NOT NULL,
    PRODUCT_ID BIGINT NOT NULL,
    CONSTRAINT DIET_RECORD_PKEY PRIMARY KEY (ID),
    CONSTRAINT DIET_RECORD_PRODUCT_FKEY FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT_ENTITY (ID),
    CONSTRAINT DIET_RECORD_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);

CREATE TABLE IF NOT EXISTS DIET_SETTING_ENTITY
(
    ID BIGINT NOT NULL,
    CREATED_DATE TIMESTAMP NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED BOOLEAN NOT NULL,
    DELETED_DATE TIMESTAMP,
    DIET_TYPE VARCHAR2(255) NOT NULL,
    MAX_CALORIES DOUBLE PRECISION NOT NULL,
    MAX_CARBOHYDRATES DOUBLE PRECISION NOT NULL,
    MAX_FATS DOUBLE PRECISION NOT NULL,
    MAX_PROTEINS DOUBLE PRECISION NOT NULL,
    CLIENT_ID BIGINT,
    CONSTRAINT DIET_SETTING_PKEY PRIMARY KEY (id),
    CONSTRAINT DIET_SETTING_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);

CREATE TABLE SPRING_SESSION (
    PRIMARY_ID              VARCHAR2 (36)           NOT NULL,
    SESSION_ID              VARCHAR2 (36),
    CREATION_TIME           NUMBER (19,0)       NOT NULL,
    LAST_ACCESS_TIME        NUMBER (19,0)       NOT NULL,
    MAX_INACTIVE_INTERVAL   NUMBER (10,0)       NOT NULL,
    EXPIRY_TIME             NUMBER (19,0)       NOT NULL,
    PRINCIPAL_NAME          VARCHAR2 (100),
    CONSTRAINT              SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
   SESSION_PRIMARY_ID  VARCHAR2 (36),
   ATTRIBUTE_NAME      VARCHAR2 (200),
   ATTRIBUTE_BYTES     BLOB,
   CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
   CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);