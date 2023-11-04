INSERT INTO CLIENT_ENTITY (ID, CREATED_DATE, UPDATED_DATE, IS_DELETED, DELETED_DATE, EMAIL, FIRST_NAME, HASH, SECOND_NAME, THIRD_NAME, UUID) VALUES (1, '2023-10-27 12:00:00', '2023-10-27 12:30:00', false, null, 'example@example.com', 'John', '874234098$821', 'Doe', 'Michael', 'client_uuid_1');

INSERT INTO BIOMETRIC_ENTITY (ID, CREATED_DATE, UPDATED_DATE, AGE, GENDER, HEIGHT, HEIGHT_MEASUREMENT, PHYSICAL_ACTIVITY, WEIGHT, WEIGHT_MEASUREMENT, CLIENT_ID) VALUES (1, '2023-10-27 12:00:00', '2023-10-27 12:30:00', 30, 'MALE', '180', 'CM', 'ACTIVE', '80', 'GK', 1);

INSERT INTO PRODUCT_ENTITY (ID, CREATED_DATE, UPDATED_DATE, IS_DELETED, DELETED_DATE, CALORIES, CARBOHYDRATES, FATS, MEASUREMENT, PRODUCER, PROTEINS, TITLE, UUID, VOLUME) VALUES (1, '2023-10-27 12:00:00', '2023-10-27 12:30:00', false, null, 250.5, 30.2, 15.8, 'GRAMS', 'company_title_1', 12.3, 'product_title_1', 'product_uuid_1', 200.0);
INSERT INTO PRODUCT_ENTITY (ID, CREATED_DATE, UPDATED_DATE, IS_DELETED, DELETED_DATE, CALORIES, CARBOHYDRATES, FATS, MEASUREMENT, PRODUCER, PROTEINS, TITLE, UUID, VOLUME) VALUES (2, '2023-10-27 13:00:00', '2023-10-27 13:30:00', false, null, 150.7, 25.5, 10.0, 'GRAMS', 'company_title_2', 8.7, 'product_title_2', 'product_uuid_2', 150.0);

INSERT INTO DIET_RECORD_ENTITY (ID, CREATED_DATE, UPDATED_DATE, CALORIES, CARBOHYDRATES, DATE, FATS, PROTEINS, TIME, VOLUME, CLIENT_ID, PRODUCT_ID) VALUES (1, '2023-10-27 12:00:00', '2023-10-27 12:30:00', 250.5, 30.2, '2023-10-27', 15.8, 12.3, '13:00:00', 200.0, 1, 1);
INSERT INTO DIET_RECORD_ENTITY (ID, CREATED_DATE, UPDATED_DATE, CALORIES, CARBOHYDRATES, DATE, FATS, PROTEINS, TIME, VOLUME, CLIENT_ID, PRODUCT_ID) VALUES (2, '2023-10-27 13:00:00', '2023-10-27 13:30:00', 150.7, 25.5, '2023-10-28', 10.0, 8.7, '14:30:00', 150.0, 1, 2);

INSERT INTO DIET_SETTING_ENTITY (ID, CREATED_DATE, UPDATED_DATE, IS_DELETED, DELETED_DATE, DIET_TYPE, MAX_CALORIES, MAX_CARBOHYDRATES, MAX_FATS, MAX_PROTEINS, CLIENT_ID) VALUES (1, '2023-10-27 12:00:00', '2023-10-27 12:30:00', false, null, 'Type A', 2000.0, 250.0, 80.0, 100.0, 1);