package ru.akvine.fitstats.exceptions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.akvine.fitstats.controllers.rest.dto.common.ErrorResponse;
import ru.akvine.fitstats.exceptions.client.ClientAlreadyExistsException;
import ru.akvine.fitstats.exceptions.client.ClientNotFoundException;
import ru.akvine.fitstats.exceptions.diet.DietSettingNotFoundException;
import ru.akvine.fitstats.exceptions.product.ProductNotFoundException;
import ru.akvine.fitstats.exceptions.security.AuthenticationException;
import ru.akvine.fitstats.exceptions.validation.ValidationException;

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

    @ExceptionHandler({AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException exception) {
        logger.error("Authentication error occurred", exception);
        ErrorResponse errorResponse = errorResponseBuilder.build(Security.AUTHENTICATE_ERROR, exception.getMessage());
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
}
