package com.corefiling.labs.analysis.impl;

import static com.corefiling.platform.instanceService.model.Fact.TypeEnum.NUMERICFACT;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;

import com.corefiling.labs.analysis.FactRequester;
import com.corefiling.labs.analysis.NumericFactValue;
import com.corefiling.labs.exception.NotFoundException;
import com.corefiling.platform.instanceService.ApiClient;
import com.corefiling.platform.instanceService.ApiException;
import com.corefiling.platform.instanceService.FactsApi;
import com.corefiling.platform.instanceService.model.NumericFact;

/** Uses the instance service to obtain the numeric facts for a filing. */
public class FactRequesterImpl implements FactRequester {

  private final FactsApi _api;

  public FactRequesterImpl(final String basePath, final String accessToken) {
    final ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(basePath).setAccessToken(accessToken);
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
                return fact.getUnit().getDenominators().isEmpty() && fact.getUnit().getNumerators().size() == 1 && fact.getUnit().getNumerators().get(0).getName().startsWith("iso4217:");
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
