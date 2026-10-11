package com.shopsphere.inventoryservice.exceptions;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<Map<String, String>> handleInsufficientStockException(
    InsufficientStockException e
  ) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
      Map.of("message", e.getMessage())
    );
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, String>> handleRuntimeException(
    RuntimeException e
  ) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
      Map.of("message", e.getMessage())
    );
  }
}
