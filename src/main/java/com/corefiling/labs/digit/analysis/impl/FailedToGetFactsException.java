/*
 *  Copyright 2017 CoreFiling S.A.R.L.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
