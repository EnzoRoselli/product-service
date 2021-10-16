package mymarket.product.controller;

import lombok.extern.slf4j.Slf4j;
import mymarket.product.exception.ErrorMessage;
import mymarket.product.exception.ProductNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.Arrays;

@Slf4j
@RestControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MissingServletRequestParameterException.class, MethodArgumentTypeMismatchException.class,
            DataIntegrityViolationException.class, MethodArgumentNotValidException.class, HttpMessageNotReadableException.class})
    public ErrorMessage badRequest(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ErrorMessage(HttpStatus.BAD_REQUEST.value(), LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false));
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({ProductNotFoundException.class, EmptyResultDataAccessException.class})
    public ErrorMessage notFound(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        log.error(Arrays.toString(ex.getStackTrace()));
        return new ErrorMessage(HttpStatus.NOT_FOUND.value(), LocalDateTime.now(),
                ex.getMessage(), request.getDescription(false));
    }
}
