package com.corefiling.labs.service.impl;

import static com.corefiling.platform.instanceService.model.Period.TypeEnum.FOREVERPERIOD;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;
import java.util.UUID;

import com.corefiling.platform.instanceService.ApiClient;
import com.corefiling.platform.instanceService.FactsApi;
import com.corefiling.platform.instanceService.FilingVersionsApi;
import com.corefiling.platform.instanceService.model.Concept;
import com.corefiling.platform.instanceService.model.Decimals;
import com.corefiling.platform.instanceService.model.Entity;
import com.corefiling.platform.instanceService.model.Fact;
import com.corefiling.platform.instanceService.model.ForeverPeriod;
import com.corefiling.platform.instanceService.model.Measure;
import com.corefiling.platform.instanceService.model.NumericFact;
import com.corefiling.platform.instanceService.model.Unit;
import com.google.common.collect.Lists;

/** Inserts a filing into the instance-service. */
public abstract class FilingInserter {

  private final ApiClient _client;

  public FilingInserter(final ApiClient client) {
    _client = client;
  }

  public UUID insert() throws Exception {
    final UUID filingVersionId = UUID.randomUUID();
    new FilingVersionsApi(_client).createFilingVersion(filingVersionId);
    new FactsApi(_client).createFacts(filingVersionId, facts());
    return filingVersionId;
  }

  private List<Fact> facts() {
    final List<Fact> facts = Lists.newArrayList();
    for (int i = 0; i < getNumberOfFacts(); ++i) {
      final NumericFact fact = new NumericFact();
      fact.setId((long) i);
      fact.setEntity(new Entity().scheme("scheme").identifier("identifier"));
      fact.setPeriod(new ForeverPeriod().type(FOREVERPERIOD));
      fact.setConcept(new Concept().name("foo:Bar"));
      fact.setDimensionValues(emptyList());
      fact.setSourceInformations(emptyList());
      fact.setDecimals(new Decimals().infinite(true));
      fact.setUnit(new Unit().numerators(singletonList(new Measure().name("iso4217:GBP"))));

      final String value = getValue(i);
      fact.setReportedValue(value);
      fact.setNumericValue(Double.valueOf(value));
    }
    return facts;
  }

  protected int getNumberOfFacts() {
    return 200;
  }

  protected abstract String getValue(int i);

}
