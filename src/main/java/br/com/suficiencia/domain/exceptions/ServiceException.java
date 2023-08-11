package br.com.suficiencia.domain.exceptions;

import org.springframework.http.HttpStatus;

public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private HttpStatus httpStatus;
    private int statusCode;

    public ServiceException(HttpStatus errorCategory, String message) {
        super(message);
        this.statusCode = errorCategory.value();
        this.httpStatus = errorCategory;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }
    public int getStatusCode() {return this.statusCode;}
}
