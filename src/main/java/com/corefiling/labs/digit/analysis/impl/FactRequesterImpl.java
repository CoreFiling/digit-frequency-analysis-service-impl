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

import static com.corefiling.platform.instanceService.model.Fact.TypeEnum.NUMERICFACT;
import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.corefiling.labs.digit.analysis.FactRequester;
import com.corefiling.labs.digit.analysis.NumericFactValue;
import com.corefiling.labs.digit.exception.NotFoundException;
import com.corefiling.platform.instanceService.ApiClient;
import com.corefiling.platform.instanceService.ApiException;
import com.corefiling.platform.instanceService.FactsApi;
import com.corefiling.platform.instanceService.model.NumericFact;

/** Uses the instance service to obtain the numeric facts for a filing. */
public class FactRequesterImpl implements FactRequester {

  private final FactsApi _api;

  public FactRequesterImpl(final String basePath, final String accessToken) {
    final ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(URI.create(basePath).resolve("v1/").toString()).setAccessToken(accessToken);
    _api = new FactsApi(apiClient);
  }

  @Override
  public List<NumericFactValue> getFacts(final UUID filingVersionId) {
    try {
      return _api.getFacts(filingVersionId, null).stream()
          .filter(fact -> fact.getType() == NUMERICFACT)
          .map(fact -> (NumericFact) fact)
          .map(fact -> {
            return (NumericFactValue) new NumericFactValue() {
              @Override
              public boolean isMonetary() {
                return fact.getUnit().getDenominators().isEmpty() && fact.getUnit().getNumerators().size() == 1 && fact.getUnit().getNumerators().get(0).getName().toLowerCase(Locale.ENGLISH).startsWith("iso4217:");
              }
              @Override
              public boolean hasValue() {
                return fact.getNumericValue() != null;
              }
              @Override
              public String getValue() {
                return fact.getReportedValue();
              }
            };
          })
          .collect(toList());
    }
    catch (final ApiException e) {
      if (HttpStatus.NOT_FOUND.value() == e.getCode()) {
        throw new NotFoundException("Could not find filing with ID: " + filingVersionId, e);
      }
      throw new FailedToGetFactsException("Failed to get facts for instance.", e);
    }

  }

}
