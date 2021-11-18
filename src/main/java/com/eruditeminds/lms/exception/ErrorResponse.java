package com.eruditeminds.lms.exception;

import com.fasterxml.jackson.annotation.JsonInclude;


/**
 * ErrorResponse pojo for exceptions
 * 
 * @author Shridha S Jalihal
 */
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
  
  private final String message;

  private String stackTrace;

}