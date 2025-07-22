package com.mishelrodri.exceptions;

import com.mishelrodri.dto.Mensaje;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Mensaje> handleCustomException(CustomException ex) {
        Mensaje mensaje = new Mensaje(ex.getMensaje());
        return new ResponseEntity<>(mensaje, ex.getStatus());
    }

}
