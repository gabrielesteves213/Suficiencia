package br.com.suficiencia.domain.exceptions;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionController extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiError> handleServiceException(ServiceException serviceException) {
        return new ResponseEntity<>(new ApiError(serviceException.getHttpStatus(), serviceException.getStatusCode(),//
                serviceException.getMessage()), serviceException.getHttpStatus() );
    }

}
