package com.mishelrodri.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class CustomException extends RuntimeException{

    private HttpStatus status;
    private String mensaje;

    public CustomException(HttpStatus status, String mensaje) {
        this.status = status;
        this.mensaje = mensaje;
    }
}
