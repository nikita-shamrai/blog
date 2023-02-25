package ua.shamray.myblogspringbootv1.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.expression.AccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {ApiRequestException.class})
    public <T extends Exception> ResponseEntity<?> apiExceptionHandler(T e){
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ApiException apiException = getApiException(e, httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public <T extends Exception> ResponseEntity<?> resourceNotFoundExceptionHandler(T e) {
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ApiException apiException = getApiException(e, httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }

    @ExceptionHandler(value = {AccessException.class, AccessDeniedException.class})
    public <T extends Exception> ResponseEntity<?> accessExceptionHandler(T e) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        ApiException apiException = getApiException(e, httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }
    @ExceptionHandler(value = {EntityExistsException.class})
    public <T extends Exception> ResponseEntity<?> entityExistsExceptionHandler(T e) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ApiException apiException = getApiException(e, httpStatus);
        return new ResponseEntity<>(apiException, httpStatus);
    }

   //This handler is to extract only "default message" from full error message in validation
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
