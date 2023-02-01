package ua.shamray.myblogspringbootv1.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
@Getter
public class ApiException {

    private final String message;
   // private final Throwable throwable;
    private final HttpStatus httpStatus;
    private final LocalDateTime localDateTime;
   // private final ZonedDateTime timestamp;

}
