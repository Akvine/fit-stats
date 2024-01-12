--liquibase formatted sql logicalFilePath:db/changelog/database-changelog.sql

--changeset lymar-sa:FIT-STATS-1-1
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'CLIENT_ENTITY' and table_schema = 'public';
CREATE TABLE CLIENT_ENTITY
(
    ID           BIGINT       NOT NULL,
    CREATED_DATE TIMESTAMP    NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED   BOOLEAN      NOT NULL,
    DELETED_DATE TIMESTAMP,
    EMAIL        VARCHAR(255) NOT NULL,
    FIRST_NAME   VARCHAR(255) NOT NULL,
    HASH         VARCHAR(255) NOT NULL,
    SECOND_NAME  VARCHAR(255) NOT NULL,
    THIRD_NAME   VARCHAR(255),
    UUID         VARCHAR(255) NOT NULL,
    CONSTRAINT CLIENT_PKEY PRIMARY KEY (id)
);
CREATE SEQUENCE SEQ_CLIENT_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-2
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'BIOMETRIC_ENTITY' and table_schema = 'public';
CREATE TABLE BIOMETRIC_ENTITY
(
    ID                 BIGINT       NOT NULL,
    CREATED_DATE       TIMESTAMP    NOT NULL,
    UPDATED_DATE       TIMESTAMP,
    AGE                INTEGER      NOT NULL,
    GENDER             VARCHAR(255),
    HEIGHT             VARCHAR(255) NOT NULL,
    HEIGHT_MEASUREMENT VARCHAR(255) NOT NULL,
    PHYSICAL_ACTIVITY  VARCHAR(255) NOT NULL,
    WEIGHT             VARCHAR(255) NOT NULL,
    WEIGHT_MEASUREMENT VARCHAR(255) NOT NULL,
    client_id          bigint,
    CONSTRAINT BIOMETRIC_PKEY PRIMARY KEY (ID),
    CONSTRAINT BIOMETRIC_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);
CREATE SEQUENCE SEQ_BIOMETRIC_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-3
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PRODUCT_ENTITY' and table_schema = 'public';
CREATE TABLE PRODUCT_ENTITY
(
    ID            BIGINT           NOT NULL,
    CREATED_DATE  TIMESTAMP        NOT NULL,
    UPDATED_DATE  TIMESTAMP,
    IS_DELETED    BOOLEAN          NOT NULL,
    DELETED_DATE  TIMESTAMP,
    CALORIES      DOUBLE PRECISION NOT NULL,
    CARBOHYDRATES DOUBLE PRECISION NOT NULL,
    FATS          DOUBLE PRECISION NOT NULL,
    MEASUREMENT   VARCHAR(64),
    PRODUCER      VARCHAR(255)     NOT NULL,
    PROTEINS      DOUBLE PRECISION NOT NULL,
    TITLE         VARCHAR(255)     NOT NULL,
    UUID          VARCHAR(255)     NOT NULL,
    VOLUME        DOUBLE PRECISION NOT NULL,
    CONSTRAINT PRODUCT_PKEY PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_PRODUCT_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-4
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'DIET_RECORD_ENTITY' and table_schema = 'public';
CREATE TABLE DIET_RECORD_ENTITY
(
    ID            BIGINT           NOT NULL,
    CREATED_DATE  TIMESTAMP        NOT NULL,
    UPDATED_DATE  TIMESTAMP,
    CALORIES      DOUBLE PRECISION NOT NULL,
    CARBOHYDRATES DOUBLE PRECISION NOT NULL,
    DATE          DATE             NOT NULL,
    FATS          DOUBLE PRECISION NOT NULL,
    PROTEINS      DOUBLE PRECISION NOT NULL,
    TIME          TIME,
    UUID          VARCHAR(255)     NOT NULL,
    VOLUME        DOUBLE PRECISION NOT NULL,
    CLIENT_ID     BIGINT           NOT NULL,
    PRODUCT_ID    BIGINT           NOT NULL,
    CONSTRAINT DIET_RECORD_PKEY PRIMARY KEY (ID),
    CONSTRAINT DIET_RECORD_PRODUCT_FKEY FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT_ENTITY (ID),
    CONSTRAINT DIET_RECORD_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);
CREATE SEQUENCE SEQ_DIET_RECORD_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-5
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'DIET_SETTING_ENTITY' and table_schema = 'public';
CREATE TABLE DIET_SETTING_ENTITY
(
    ID                BIGINT           NOT NULL,
    CREATED_DATE      TIMESTAMP        NOT NULL,
    UPDATED_DATE      TIMESTAMP,
    IS_DELETED        BOOLEAN          NOT NULL,
    DELETED_DATE      TIMESTAMP,
    DIET_TYPE         VARCHAR(255)     NOT NULL,
    MAX_CALORIES      DOUBLE PRECISION NOT NULL,
    MAX_CARBOHYDRATES DOUBLE PRECISION NOT NULL,
    MAX_FATS          DOUBLE PRECISION NOT NULL,
    MAX_PROTEINS      DOUBLE PRECISION NOT NULL,
    CLIENT_ID         BIGINT,
    CONSTRAINT DIET_SETTING_PKEY PRIMARY KEY (id),
    CONSTRAINT DIET_SETTING_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);
CREATE SEQUENCE SEQ_DIET_SETTING_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-6
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'SPRING_SESSION' and table_schema = 'public';
CREATE TABLE SPRING_SESSION
(
    PRIMARY_ID            VARCHAR(36)    NOT NULL,
    SESSION_ID            VARCHAR(36),
    CREATION_TIME         NUMERIC(19, 0) NOT NULL,
    LAST_ACCESS_TIME      NUMERIC(19, 0) NOT NULL,
    MAX_INACTIVE_INTERVAL NUMERIC(10, 0) NOT NULL,
    EXPIRY_TIME           NUMERIC(19, 0) NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
);

--changeset lymar-sa:FIT-STATS-1-7
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'SPRING_SESSION_ATTRIBUTES' and table_schema = 'public';
CREATE TABLE SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID VARCHAR(36),
    ATTRIBUTE_NAME     VARCHAR(200),
    ATTRIBUTE_BYTES    BYTEA,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
);

--changeset lymar-sa:FIT-STATS-1-8
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'AUTH_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE AUTH_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    PWD_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT AUTH_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_AUTH_ACTION_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-9
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'REGISTRATION_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE REGISTRATION_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    STATE                     VARCHAR(32)                         NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT REGISTRATION_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_REGISTRATION_ACTION_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-10
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'TAG_ENTITY' and table_schema = 'public';
CREATE TABLE TAG_ENTITY
(
    ID           BIGINT       NOT NULL,
    CREATED_DATE TIMESTAMP    NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED   BOOLEAN      NOT NULL,
    DELETED_DATE TIMESTAMP,
    TITLE        VARCHAR(128) NOT NULL,
    TYPE         VARCHAR(128) NOT NULL,
    CONSTRAINT TAG_ENTITY_PKEY PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_TAG_ENTITY START WITH 100 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-11
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'BLOCKED_CREDENTIALS_ENTITY' and table_schema = 'public';
CREATE TABLE BLOCKED_CREDENTIALS_ENTITY
(
    ID               BIGINT                              NOT NULL,
    LOGIN            VARCHAR(64)                         NOT NULL,
    BLOCK_START_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    BLOCK_END_DATE   TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    CONSTRAINT BLOCKED_CREDENTIALS_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_BLOCKED_CREDENTIALS_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-12
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'OTP_COUNTER_ENTITY' and table_schema = 'public';
CREATE TABLE OTP_COUNTER_ENTITY
(
    ID           BIGINT                              NOT NULL,
    LOGIN        VARCHAR(64)                         NOT NULL,
    LAST_UPDATED TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    OTP_VALUE    BIGINT    DEFAULT 1                 NOT NULL,
    CONSTRAINT OTP_COUNTER_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_OTP_COUNTER_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-13
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PRODUCT_TAG' and table_schema = 'public';
CREATE TABLE PRODUCT_TAG
(
    PRODUCT_ID  BIGINT NOT NULL,
    TAG_ID BIGINT NOT NULL,
    PRIMARY KEY (PRODUCT_ID, TAG_ID),
    CONSTRAINT FK_PRODUCT_TAG_PRODUCT FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT_ENTITY (ID),
    CONSTRAINT FK_PRODUCT_TAG_TAG FOREIGN KEY (TAG_ID) REFERENCES TAG_ENTITY (ID)
);

--changeset lymar-sa:FIT-STATS-1-14
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(table_name) = 'PRODUCT_ENTITY' and upper(column_name) = 'VOL' AND table_schema = 'public'
ALTER TABLE PRODUCT_ENTITY ADD VOL DOUBLE PRECISION NOT NULL DEFAULT 0;

--changeset lymar-sa:FIT-STATS-1-15
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(table_name) = 'DIET_RECORD_ENTITY' and upper(column_name) = 'VOL' AND table_schema = 'public'
ALTER TABLE DIET_RECORD_ENTITY ADD VOL DOUBLE PRECISION NOT NULL DEFAULT 0;

--changeset lymar-sa:FIT-STATS-1-16
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'ACCESS_RESTORE_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE ACCESS_RESTORE_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    STATE                     VARCHAR(32)                         NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT ACCESS_RESTORE_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_ACCESS_RESTORE_ACTION_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-17
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PROFILE_DELETE_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE PROFILE_DELETE_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    PWD_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT PROFILE_DELETE_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_PROFILE_DELETE_ACTION_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-18
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PROFILE_CHANGE_EMAIL_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE PROFILE_CHANGE_EMAIL_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    NEW_EMAIL                 VARCHAR(64)                         NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    PWD_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT PROFILE_CHANGE_EMAIL_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_PROFILE_CHANGE_EMAIL_ENTITY_ACTION START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-19
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PROFILE_CHANGE_PASSWORD_ACTION_ENTITY' and table_schema = 'public';
CREATE TABLE PROFILE_CHANGE_PASSWORD_ACTION_ENTITY
(
    ID                        BIGINT                              NOT NULL,
    SESSION_ID                VARCHAR(144)                        NOT NULL,
    LOGIN                     VARCHAR(64)                         NOT NULL,
    NEW_HASH                  VARCHAR(255)                        NOT NULL,
    STARTED_DATE              TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
    ACTION_EXPIRED_AT         TIMESTAMP                           NOT NULL,
    PWD_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_COUNT_LEFT            INTEGER                             NOT NULL,
    OTP_NUMBER                INTEGER,
    OTP_LAST_UPDATE           TIMESTAMP,
    OTP_EXPIRED_AT            TIMESTAMP,
    OTP_INVALID_ATTEMPTS_LEFT INTEGER                             NOT NULL,
    OTP_VALUE                 VARCHAR(32),
    CONSTRAINT PROFILE_CHANGE_PASSWORD_ACTION_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_PROFILE_CHANGE_PASSWORD_ACTION_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-20
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'WEIGHT_RECORD_ENTITY' and table_schema = 'public';
CREATE TABLE WEIGHT_RECORD_ENTITY (
    ID            BIGINT           NOT NULL,
    CREATED_DATE  TIMESTAMP        NOT NULL,
    UPDATED_DATE  TIMESTAMP,
    DATE          DATE             NOT NULL,
    WEIGHT_VALUE  VARCHAR(255)     NOT NULL,
    CLIENT_ID     BIGINT           NOT NULL,
    CONSTRAINT WEIGHT_RECORD_PKEY PRIMARY KEY (ID),
    CONSTRAINT WEIGHT_RECORD_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);
CREATE SEQUENCE SEQ_WEIGHT_RECORD_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-21
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'TELEGRAM_AUTH_CODE_ENTITY' and table_schema = 'public';
CREATE TABLE TELEGRAM_AUTH_CODE_ENTITY (
  ID            BIGINT           NOT NULL,
  CODE          VARCHAR(255)     NOT NULL,
  CREATED_DATE  TIMESTAMP        NOT NULL,
  EXPIRED_AT    TIMESTAMP        NOT NULL,
  CLIENT_ID     BIGINT           NOT NULL,
  CONSTRAINT TELEGRAM_AUTH_CODE_PKEY PRIMARY KEY (ID),
  CONSTRAINT TELEGRAM_AUTH_CODE_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);
CREATE SEQUENCE SEQ_TELEGRAM_AUTH_CODE_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-22
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'TELEGRAM_SUBSCRIPTION_ENTITY' and table_schema = 'public';
CREATE TABLE TELEGRAM_SUBSCRIPTION_ENTITY (
   ID             BIGINT           NOT NULL,
   TELEGRAM_ID    BIGINT           NOT NULL,
   CREATED_DATE   TIMESTAMP        NOT NULL,
   UPDATED_DATE   TIMESTAMP,
   CHAT_ID        VARCHAR(255)     NOT NULL,
   IS_DELETED     BOOLEAN          NOT NULL,
   DELETED_DATE   TIMESTAMP,
   CLIENT_ID      BIGINT            NOT NULL,
   CONSTRAINT TELEGRAM_SUBSCRIPTION_PKEY PRIMARY KEY (ID),
   CONSTRAINT TELEGRAM_SUBSCRIPTION_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);
CREATE SEQUENCE SEQ_TELEGRAM_SUBSCRIPTION_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-23
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'TELEGRAM_DIET_NOTIFICATION_SUBSCRIPTION_ENTITY' and table_schema = 'public';
CREATE TABLE TELEGRAM_DIET_NOTIFICATION_SUBSCRIPTION_ENTITY (
  ID                                        BIGINT           NOT NULL,
  TELEGRAM_ID                               BIGINT           NOT NULL,
  DIET_NOTIFICATION_SUBSCRIPTION_TYPE       VARCHAR(255)     NOT NULL,
  IS_PROCESSED                              BOOLEAN          NOT NULL,
  CLIENT_ID                                 BIGINT           NOT NULL,
  CONSTRAINT TELEGRAM_DIET_NOTIFICATION_SUBSCRIPTION_PKEY PRIMARY KEY (ID),
  CONSTRAINT TELEGRAM_DIET_NOTIFICATION_SUBSCRIPTION_CLIENT_FKEY FOREIGN KEY (CLIENT_ID) REFERENCES CLIENT_ENTITY (ID)
);
CREATE SEQUENCE SEQ_TELEGRAM_DIET_NOTIFICATION_SUBSCRIPTION_ENTITY START WITH 1 INCREMENT BY 1000;