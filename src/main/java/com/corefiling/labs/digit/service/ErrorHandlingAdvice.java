package com.corefiling.labs.digit.service;

import java.util.Locale;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.corefiling.labs.digit.analysis.impl.FailedToGetFactsException;
import com.corefiling.labs.digit.exception.NotFoundException;
import com.corefiling.labs.digit.model.ErrorResponse;
import com.corefiling.platform.instanceService.ApiException;

/**
 * Handles common errors.
 */
@ControllerAdvice
@ResponseBody
@Order(ErrorHandlingAdvice.MID_ORDER)
public class ErrorHandlingAdvice {

  static final int MID_ORDER = 0;
  private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandlingAdvice.class);

  public static ResponseEntity<ErrorResponse> makeErrorResponse(final HttpServletRequest request, final Throwable exception, final HttpStatus httpStatus) {
    final String message = exception.getMessage() == null ? "No message available" : exception.getMessage();
    return makeErrorResponse(request, exception, httpStatus, message);
  }

  public static ResponseEntity<ErrorResponse> makeErrorResponse(final HttpServletRequest request, final Throwable exception, final HttpStatus httpStatus, final String message) {
    return makeErrorResponse(request, exception, httpStatus, message, System.currentTimeMillis(), request.getServletPath());
  }

  public static ResponseEntity<ErrorResponse> makeErrorResponse(final HttpServletRequest request, final Throwable exception, final HttpStatus httpStatus, final String message, final long timestamp, final String path) {
    if (httpStatus.is5xxServerError()) {
      LOGGER.error("HTTP Server Error", exception);
    }
    else {
      LOGGER.debug("HTTP Client Error", exception);
    }
    return new ResponseEntity<>(new ErrorResponse()
      .status(Optional.of(httpStatus.value()))
      .error(Optional.of(httpStatus.getReasonPhrase()))
      .exception(Optional.ofNullable(exception).map(ex -> ex.getClass().getName()))
      .message(Optional.of(message))
      .path(Optional.of(path))
      .timestamp(Optional.of(timestamp)), httpStatus);
  }

  @Autowired
  private MessageSource _messages;

  /**
   * Handles binding errors from the validator (e.g. request parameters).
   */
  @ExceptionHandler(BindException.class)
  public ResponseEntity<ErrorResponse> handleBindingErrors(final HttpServletRequest request, final BindException ex) {
    // [tmm] Just take the first field error for now.
    final FieldError fieldError = ex.getBindingResult().getFieldError();
    final String detail = _messages.getMessage(fieldError, Locale.getDefault());
    final String message = String.format("Invalid value for parameter '%s': %s.", fieldError.getField(), detail);
    return makeErrorResponse(request, ex, HttpStatus.BAD_REQUEST, message);
  }

  @ExceptionHandler(FailedToGetFactsException.class)
  public ResponseEntity<ErrorResponse> handleFailedToGetFactsException(final HttpServletRequest request, final FailedToGetFactsException e) {
    final int code = Optional.ofNullable(e.getApiException()).map(ApiException::getCode).orElse(500);
    return makeErrorResponse(request, e, HttpStatus.valueOf(code));
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFoundException(final HttpServletRequest request, final NotFoundException e) {
    return makeErrorResponse(request, e, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ErrorResponse> handleInvalidParameterException(final HttpServletRequest request, final MethodArgumentTypeMismatchException exception) {
    final Throwable reportException = exception.getCause() == null ? exception : exception.getCause();
    return makeErrorResponse(request, reportException, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ErrorResponse> handleMissingServletRequestPartException(final HttpServletRequest request, final MissingServletRequestPartException exception) {
    return makeErrorResponse(request, exception, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ResponseEntity<ErrorResponse> handleMissingParameterException(final HttpServletRequest request, final MissingServletRequestParameterException exception) {
    return makeErrorResponse(request, exception, HttpStatus.BAD_REQUEST);
  }

  /**
   * Handles unsupported content types.
   */
  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleUnsupportedContentType(final HttpServletRequest request, final HttpMediaTypeNotSupportedException ex) {
    return makeErrorResponse(request, ex, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
  }

  /**
   * Handles problems with the request body.
   */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleUnreadableRequest(final HttpServletRequest request, final HttpMessageNotReadableException ex) {
    final String error = ex.getMessage().startsWith("Required request body is missing:") ? "Request body must contain a JSON specification." : "Invalid request body.";
    return makeErrorResponse(request, ex, HttpStatus.BAD_REQUEST, error);
  }

}
