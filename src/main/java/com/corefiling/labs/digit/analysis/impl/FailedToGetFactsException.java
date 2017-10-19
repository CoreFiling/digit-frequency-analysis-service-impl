package com.corefiling.labs.digit.analysis.impl;

import com.corefiling.platform.instanceService.ApiException;

/** Failed to obtain the facts from the instance service. */
public class FailedToGetFactsException extends RuntimeException {

  private static final long serialVersionUID = 1L;
  private final ApiException _apiException;

  public FailedToGetFactsException(final String message, final ApiException ex) {
    super(message, ex);
    _apiException = ex;
  }

  public ApiException getApiException() {
    return _apiException;
  }

}
