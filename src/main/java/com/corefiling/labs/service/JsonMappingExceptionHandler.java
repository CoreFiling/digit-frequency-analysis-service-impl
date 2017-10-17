package com.corefiling.labs.service;

import static com.corefiling.labs.service.ErrorHandlingAdvice.makeErrorResponse;

import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.corefiling.labs.model.ErrorResponse;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.google.common.base.Throwables;

/***/
public class JsonMappingExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(JsonMappingExceptionHandler.class);

  /**
   * Handles the different types of JSON mapping problem.
   */
  public final ResponseEntity<ErrorResponse> handleJsonMappingException(final HttpServletRequest request, final JsonMappingException ex) {
    LOGGER.error("An exception occurred when deserialising JSON request", ex);
    final Throwable rootCause = Throwables.getRootCause(ex);
    if (rootCause instanceof JsonParseException) {
      // Invalid JSON syntax.
      return handleMalformedJson(request, (JsonParseException) rootCause);
    }
    else {
      final String fragmentFromRootCause = getFragmentFromRootCause(rootCause);
      final String fragment = fragmentFromRootCause != null ? fragmentFromRootCause : "Entity contains invalid content";
      return invalidRequestBody(request, ex, ex.getPath().size() <= 1 ? fragment + "." : fragment + " at path: " + formatPath(ex.getPath()));
    }
  }

  /** @param rootCause - guess. */
  protected String getFragmentFromRootCause(final Throwable rootCause) {
    return null;
  }

  /**
   * Handles malformed JSON.
   */
  private ResponseEntity<ErrorResponse> handleMalformedJson(final HttpServletRequest request, final JsonParseException ex) {
    final JsonLocation location = ex.getLocation();
    return invalidRequestBody(request, ex, "Request body contains invalid JSON at line " + location.getLineNr() + ", column " + location.getColumnNr() + ".");
  }

  private String formatPath(final List<Reference> references) {
    final StringBuilder path = new StringBuilder();
    for (final Reference ref : references) {
      if (ref.getFrom() instanceof Collection) {
        path.append('[').append(ref.getIndex()).append(']');
      }
      else {
        if (path.length() > 0) {
          path.append('.');
        }
        path.append(ref.getFieldName());
      }
    }
    return path.toString();
  }

  private ResponseEntity<ErrorResponse> invalidRequestBody(final HttpServletRequest request, final Throwable exception, final String message) {
    return makeErrorResponse(request, exception, HttpStatus.BAD_REQUEST, message);
  }

}
