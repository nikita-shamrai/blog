package ua.shamray.myblogspringbootv1.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler /*extends ResponseEntityExceptionHandler*/ {

    @ExceptionHandler(value = {ApiRequestException.class})
    public ResponseEntity<?> apiExceptionHandler(ApiRequestException e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = getApiException(e, httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<?> resourceNotFoundExceptionHandler(ResourceNotFoundException e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = getApiException(e, httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {AccessException.class})
    public ResponseEntity<?> accessExceptionHandler(AccessException e) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ApiException apiException = getApiException(e, httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<?> handleMethodArgumentNotValid(MethodArgumentNotValidException e){
        HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;
        List<String> errors = e
                .getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        ApiException apiException = new ApiException(
                errors,
                httpStatus,
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiException, httpStatus);
    }

    private <T extends Throwable> ApiException getApiException(T e, HttpStatus httpStatus) {
        return new ApiException(
                e.getMessage(),
                httpStatus,
                LocalDateTime.now()
        );
    }

}
