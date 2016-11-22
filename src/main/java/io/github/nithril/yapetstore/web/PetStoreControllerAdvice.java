package io.github.nithril.yapetstore.web;

import static java.util.Collections.singletonMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;

/**
 * Created by nlabrot on 21/11/16.
 */
@ControllerAdvice
public class PetStoreControllerAdvice {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity accessDenied(){
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(singletonMap("message" , "Forbidden"));
  }
}
