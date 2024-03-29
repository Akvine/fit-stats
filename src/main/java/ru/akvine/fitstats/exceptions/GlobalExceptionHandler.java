package ru.akvine.fitstats.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.akvine.fitstats.controllers.rest.dto.common.ErrorResponse;
import ru.akvine.fitstats.exceptions.barcode.BarCodeAlreadyExistsException;
import ru.akvine.fitstats.exceptions.barcode.BarCodeNotFoundException;
import ru.akvine.fitstats.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.fitstats.exceptions.client.ClientNotFoundException;
import ru.akvine.fitstats.exceptions.diet.DietRecordNotFoundException;
import ru.akvine.fitstats.exceptions.diet.DietRecordsNotUniqueResultException;
import ru.akvine.fitstats.exceptions.diet.DietSettingNotFoundException;
import ru.akvine.fitstats.exceptions.diet.ProductsNotUniqueResultException;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;
import ru.akvine.fitstats.exceptions.security.*;
import ru.akvine.fitstats.exceptions.security.registration.RegistrationNotStartedException;
import ru.akvine.fitstats.exceptions.security.registration.RegistrationWrongStateException;
import ru.akvine.fitstats.exceptions.telegram.TelegramAuthCodeNotFoundException;
import ru.akvine.fitstats.exceptions.validation.ValidationException;
import ru.akvine.fitstats.exceptions.weight.WeightRecordNotFoundException;
import ru.akvine.fitstats.utils.SecurityUtils;

import javax.servlet.http.HttpServletRequest;

import static ru.akvine.fitstats.exceptions.CommonErrorCodes.*;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private final ErrorResponseBuilder errorResponseBuilder;

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        logger.error("general error occurred", exception);
        ErrorResponse errorResponse = errorResponseBuilder
                .build(GENERAL_ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorResponse> handleException(ValidationException exception) {
        logger.info("validation error occurred", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(exception.getCode(), exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ClientAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleClientAlreadyExistsException(ClientAlreadyExistsException exception) {
        logger.info("Client already exists error occurred", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(Client.CLIENT_ALREADY_EXISTS_ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ClientNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleClientNotFoundException(ClientNotFoundException exception) {
        logger.info("Client not found error occurred", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(Client.CLIENT_NOT_FOUND_ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DietSettingNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleDietSettingNotFoundException(DietSettingNotFoundException exception) {
        logger.info("Diet setting not found error occurred", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(Diet.DIET_SETTING_NOT_FOUND_ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleProductNotFoundException(ProductNotFoundException exception) {
        logger.info("Product not found error occurred", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(Product.PRODUCT_NOT_FOUND_ERROR, exception.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException exception, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.info("Method argument is not presented", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(Validation.FIELD_NOT_PRESENTED_ERROR, "Field is not presented!");
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OtpInvalidAttemptException.class})
    public ResponseEntity<ErrorResponse> handleOtpInvalidAttemptException(OtpInvalidAttemptException exception) {
        logger.info("Login=[{}] entered wrong otp! Attempts left=[{}], exceptionMessage=[{}]",
                exception.getLogin(), exception.getAttemptsLeft(), exception.getMessage());
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.INVALID_ATTEMPT_ERROR,
                Security.INVALID_ATTEMPT_ERROR
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoSessionException.class})
    public ResponseEntity<ErrorResponse> handleNoSessionException(NoSessionException exception) {
        logger.info("No active session found.", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                NO_SESSION,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(WrongSessionException.class)
    public ResponseEntity<ErrorResponse> handleWrongSessionException(WrongSessionException exception) {
        logger.info("Invalid session id found!");
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.INVALID_SESSION_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationNotStartedException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationNotStartedException(RegistrationNotStartedException exception) {
        logger.info("Registration not started yet", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.ACTION_NOT_STARTED_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationWrongStateException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationWrongStateException(RegistrationWrongStateException exception) {
        logger.warn("Registration in wrong state", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.INVALID_STATE_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OtpExpiredException.class)
    public ResponseEntity<ErrorResponse> handleOtpExpiredException(OtpExpiredException exception) {
        logger.info("One-time-password is expired, another one should be generated: message={}, otpCountLeft={}",
                exception.getMessage(), exception.getOtpCountLeft());
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.OTP_EXPIRED_ERROR,
                "One time password is expired! You need to receive another one! OtpCountLeft=" + exception.getOtpCountLeft()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoMoreNewOtpAvailableException.class)
    public ResponseEntity<ErrorResponse> handleNoMoreNewOtpAvailableException(NoMoreNewOtpAvailableException exception, HttpServletRequest request) {
        logger.info("No more new one-time-password can be generated: userWasBlocked={}, exceptionMessage={} ",
                exception.userWasBlocked(), exception.getMessage());
        if (exception.userWasBlocked()) {
            SecurityUtils.doLogout(request);
        }
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.LIMIT_REACHED_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ActionNotStartedException.class})
    public ResponseEntity<ErrorResponse> handleActionNotStartedException(ActionNotStartedException ex) {
        logger.info("Action not initiated, wrong step. Exception message={}", ex.getMessage());
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.ACTION_NOT_STARTED_ERROR,
                ex.getMessage());
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException exception) {
        logger.info("Bad credentials", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.BAD_CREDENTIALS_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DietRecordNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleDietRecordNotFoundException(DietRecordNotFoundException exception) {
        logger.info("Diet record not found exception", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Diet.DIET_RECORD_NOT_FOUND_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OtpAuthRequiredException.class})
    public ResponseEntity<ErrorResponse> handleOtpAuthRequiredException(OtpAuthRequiredException exception) {
        logger.info("Otp auth required exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.OTP_AUTH_REQUIRED,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BlockedCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleBlockedCredentialsException(BlockedCredentialsException exception) {
        logger.info("Blocked credentials exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Security.BLOCKED_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({WeightRecordNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleWeightRecordNotFoundException(WeightRecordNotFoundException exception) {
        logger.info("Weight record not found exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Weight.WEIGHT_RECORD_NOT_FOUND_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({TelegramAuthCodeNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleTelegramAuthCodeNotFoundException(TelegramAuthCodeNotFoundException exception) {
        logger.info("Telegram auth code not found exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Telegram.TELEGRAM_AUTH_CODE_NOT_FOUND_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ProductsNotUniqueResultException.class})
    public ResponseEntity<ErrorResponse> handleProductsNotUniqueResultException(ProductsNotUniqueResultException exception) {
        logger.info("Products not unique result exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Diet.PRODUCTS_NOT_UNIQUE_RESULT_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({DietRecordsNotUniqueResultException.class})
    public ResponseEntity<ErrorResponse> handleDietRecordsNotUniqueResultException(DietRecordsNotUniqueResultException exception) {
        logger.info("Diet records not unique result exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                Diet.DIET_RECORDS_NOT_UNIQUE_RESULT_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        logger.info("Illegal argument exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(Validation.FIELD_INVALID_ERROR, "Field is not presented!");
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BarCodeNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleBarCodeNotFoundException(BarCodeNotFoundException exception) {
        logger.info("Barcode not found exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                BarCode.BAR_CODE_NOT_FOUND_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BarCodeAlreadyExistsException.class})
    public ResponseEntity<ErrorResponse> handleBarCodeAlreadyExistsException(BarCodeAlreadyExistsException exception) {
        logger.info("Barcode already exists exception=", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(
                BarCode.BAR_CODE_ALREADY_EXISTS_ERROR,
                exception.getMessage()
        );
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
