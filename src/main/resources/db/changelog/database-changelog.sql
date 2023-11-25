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
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'CATEGORY_ENTITY' and table_schema = 'public';
CREATE TABLE CATEGORY_ENTITY
(
    ID           BIGINT       NOT NULL,
    CREATED_DATE TIMESTAMP    NOT NULL,
    UPDATED_DATE TIMESTAMP,
    IS_DELETED   BOOLEAN      NOT NULL,
    DELETED_DATE TIMESTAMP,
    TITLE        VARCHAR(128) NOT NULL,
    TYPE         VARCHAR(128) NOT NULL,
    CONSTRAINT CATEGORY_ENTITY_PKEY PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_CATEGORY_ENTITY START WITH 100 INCREMENT BY 1000;

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
    VALUE        BIGINT    DEFAULT 1                 NOT NULL,
    CONSTRAINT OTP_COUNTER_PK PRIMARY KEY (ID)
);
CREATE SEQUENCE SEQ_OTP_COUNTER_ENTITY START WITH 1 INCREMENT BY 1000;

--changeset lymar-sa:FIT-STATS-1-13
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where upper(table_name) = 'PRODUCT_CATEGORY' and table_schema = 'public';
CREATE TABLE PRODUCT_CATEGORY
(
    PRODUCT_ID  BIGINT NOT NULL,
    CATEGORY_ID BIGINT NOT NULL,
    PRIMARY KEY (PRODUCT_ID, CATEGORY_ID),
    CONSTRAINT FK_PRODUCT_CATEGORY_PRODUCT FOREIGN KEY (PRODUCT_ID) REFERENCES PRODUCT_ENTITY (ID),
    CONSTRAINT FK_PRODUCT_CATEGORY_CATEGORY FOREIGN KEY (CATEGORY_ID) REFERENCES CATEGORY_ENTITY (ID)
);

--changeset lymar-sa:FIT-STATS-1-14
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from CATEGORY_ENTITY;
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (1, 'Фрукты', 'FRUITS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (2, 'Овощи', 'VEGETABLES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (3, 'Зеленые овощи', 'GREEN_VEGETABLES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (4, 'Коренья', 'ROOTS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (5, 'Рис', 'RICE', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (6, 'Макароны', 'PASTA', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (7, 'Крупы', 'CEREALS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (8, 'Мучное', 'FLOUR', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (9, 'Молочное', 'MILK', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (10, 'Йогурт', 'YOGURT', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (11, 'Творожные', 'COTTAGE_CHEESE', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (12, 'Говядина', 'BEEF', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (13, 'Курица', 'CHICKEN', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (14, 'Индейка', 'TURKEY', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (15, 'Рыбное', 'FISH', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (16, 'Свинина', 'PORK', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (17, 'Замороженные овощи', 'FROZEN_VEGETABLES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (18, 'Замороженные фруты', 'FROZEN_FRUITS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (19, 'Замороженные полуфабрикаты', 'FROZEN_SEMI_FINISHED_PRODUCTS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (20, 'Сахар', 'SUGAR', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (21, 'Соленое', 'SALT', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (22, 'Мед', 'HONEY', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (23, 'Подсолнечное масло', 'SUNFLOWER_OIL', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (24, 'Вода', 'WATER', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (25, 'Соки', 'JUICES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (26, 'Чаи', 'TEA', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (27, 'Кофе', 'COFFEE', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (28, 'Органические продукты', 'ORGANIC_PRODUCTS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (29, 'Безглютеновые продукты', 'GLUTEN_FREE_PRODUCTS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (30, 'Протеиновые продукты', 'PROTEIN_PRODUCTS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (31, 'Детские кашы', 'BABY_PORRIDGE', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (32, 'Детские соки', 'BABY_JUICES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (33, 'Детское печенье', 'CHILDREN_COOKIES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (34, 'Копчености', 'SMOKED_MEATS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (35, 'Маслины', 'OLIVES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (36, 'Крекеры', 'CRACKERS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (37, 'Шоколад', 'CHOCOLATE', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (38, 'Конфеты', 'CANDIES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (39, 'Печенье', 'COOKIES', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (40, 'Зефир', 'MARSHMALLOWS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (41, 'Чипсы', 'CHIPS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (42, 'Газированные напитки', 'CARBONATED_DRINKS', '2023-11-19', false);
INSERT INTO CATEGORY_ENTITY (ID, TITLE, TYPE, CREATED_DATE, IS_DELETED)
VALUES (43, 'Энергетики', 'ENERGETICS', '2023-11-19', false);

--changeset lymar-sa:FIT-STATS-1-15
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(table_name) = 'PRODUCT_ENTITY' and upper(column_name) = 'VOL' AND table_schema = 'public'
ALTER TABLE PRODUCT_ENTITY ADD VOL DOUBLE PRECISION NOT NULL DEFAULT 0;

--changeset lymar-sa:FIT-STATS-1-16
--preconditions onFail:MARK_RAN onError:HALT onUpdateSQL:FAIL
--precondition-sql-check expectedResult:0 select count(*) from information_schema.columns where upper(table_name) = 'DIET_RECORD_ENTITY' and upper(column_name) = 'VOL' AND table_schema = 'public'
ALTER TABLE DIET_RECORD_ENTITY ADD VOL DOUBLE PRECISION NOT NULL DEFAULT 0;

--changeset lymar-sa:FIT-STATS-1-17
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