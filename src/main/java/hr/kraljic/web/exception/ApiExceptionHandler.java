package hr.kraljic.web.exception;

import hr.kraljic.web.security.error.ApiError;
import hr.kraljic.web.util.MessagesUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Hvaca specificirane iznimke te ih uniformno maskira u ApiError model i vraca u odgovoru
 */
@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {
    /**
     * Hvaca sve iznimke koje nasljedjuju klasu ApiException, najcesce se bacaju u servisima
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {ApiException.class})
    @ResponseBody
    public final ResponseEntity<Object> handleApiException(ApiException ex) {
        ApiError apiError = new ApiError(ex.getHttpStatus(), ex.getMessage(), ex);

        return buildResponseEntity(apiError);
    }

    /**
     * Hvaca iznimke koje se dogadjaju prilikom validacije zahtjeva @Valid
     *
     * @param ex
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                "VALIDATION_ERROR", ex);

        apiError.addValidationErrors(ex.getBindingResult().getFieldErrors());

        return buildResponseEntity(apiError);
    }

    /**
     * Hvaca iznimku kada je zaprimljen zahtjev s ne vazecim formatom tijela zahtjeva
     *
     * @param ex
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST,
                MessagesUtil.get("apiException.requestBodyMissing"), ex);

        return buildResponseEntity(apiError);
    }

    /**
     * Hvaca iznimke koje nastaju prilikom poziva ne podrzane http metode
     * nad nekim resursom
     *
     * @param ex
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND,
                MessagesUtil.get("apiException.notFound"), ex);

        return buildResponseEntity(apiError);
    }

    /**
     * Hvaca iznimke prilikom pokusaja pristupanja zabranjenom resursu
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseBody
    public final ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex) {
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN,
                MessagesUtil.get("apiException.forbidden"), ex);

        return buildResponseEntity(apiError);
    }


    /**
     * Hvaca sve ostale neocekivane iznimke
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(value = {Exception.class})
    @ResponseBody
    public final ResponseEntity<Object> handleOtherExceptions(Exception ex) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                MessagesUtil.get("apiException.internalServerError"), ex);

        return buildResponseEntity(apiError);
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }
}
