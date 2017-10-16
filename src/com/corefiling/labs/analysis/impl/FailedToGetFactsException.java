package com.corefiling.labs.analysis.impl;

import com.corefiling.platform.instanceService.ApiException;

/** Failed to obtain the facts from the instance service. */
public class FailedToGetFactsException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public FailedToGetFactsException(final String message, final ApiException ex) {
    super(message, ex);
  }

}
