package com.example.demo.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ApiExceptionAdvice {

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<?> badRequest(IllegalArgumentException ex){
    return ResponseEntity.badRequest().body(Map.of("ok", false, "message", ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> unhandled(Exception ex){
    // Aquí podrías loguear ex.printStackTrace();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(Map.of(
                    "ok", false,
                    "message", "Error inesperado en el servidor",
                    "detail", ex.getClass().getSimpleName() + ": " + (ex.getMessage()==null?"":ex.getMessage())
            ));
  }
}
