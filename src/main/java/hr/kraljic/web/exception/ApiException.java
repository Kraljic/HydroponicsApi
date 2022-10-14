package hr.kraljic.web.exception;

import org.springframework.http.HttpStatus;

public class ApiException extends RuntimeException {
    private HttpStatus httpStatus;

    public ApiException(String message, HttpStatus status) {
        super(message);
        httpStatus = status;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
