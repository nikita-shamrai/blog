package ua.shamray.myblogspringbootv1.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiException {
    private String message;
    private List<String> errors;
    private final HttpStatus httpStatus;
    private final LocalDateTime timestamp;

    public ApiException(String message, HttpStatus httpStatus, LocalDateTime timestamp) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }

    public ApiException(List<String> errors, HttpStatus httpStatus, LocalDateTime timestamp) {
        this.errors = errors;
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}
