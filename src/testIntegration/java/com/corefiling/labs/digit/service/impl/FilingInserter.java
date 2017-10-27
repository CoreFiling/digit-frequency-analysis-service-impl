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

package com.corefiling.labs.digit.service.impl;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.corefiling.platform.instanceService.ApiClient;
import com.corefiling.platform.instanceService.FactsApi;
import com.corefiling.platform.instanceService.FilingVersionsApi;
import com.corefiling.platform.instanceService.model.Concept;
import com.corefiling.platform.instanceService.model.Decimals;
import com.corefiling.platform.instanceService.model.Entity;
import com.corefiling.platform.instanceService.model.Fact;
import com.corefiling.platform.instanceService.model.Fact.TypeEnum;
import com.corefiling.platform.instanceService.model.ForeverPeriod;
import com.corefiling.platform.instanceService.model.Measure;
import com.corefiling.platform.instanceService.model.NumericFact;
import com.corefiling.platform.instanceService.model.Unit;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/** Inserts a filing into the instance-service. */
public abstract class FilingInserter implements AutoCloseable {

  private static final String DATA_SET = "3c69b1c1-fc3d-46e0-89cc-3544d9d24aa5";

  private static final String FILING_ID = "e86a7669-caa3-4fde-97c9-43d0cb30d7b9";

  public static final int FACT_COUNT = 200;

  private final ApiClient _client;
  private final KeycloakProtectedResources _protectedResources;
  private final Set<String> _createdResources = Sets.newLinkedHashSet();

  public FilingInserter(final ApiClient client, final KeycloakProtectedResources protectedResources) {
    _client = client;
    _protectedResources = protectedResources;
  }

  public UUID insert() throws Exception {
    final UUID filingVersionId = UUID.randomUUID();
    final String resourceID = _protectedResources.save(new ProtectedResource() {
      @Override
      public String getURI() throws Exception {
        return "/data-set/" + DATA_SET + "/filing/" + FILING_ID + "/filing-version/" + filingVersionId;
      }
      @Override
      public String getType() {
        return "urn:platform.corefiling.com:filing-version";
      }
      @Override
      public Set<String> getScopes() {
        return ImmutableSet.of("urn:platform.corefiling.com:scope:filing-version:read");
      }
      @Override
      public String getName() {
        return getType() + ":" + filingVersionId.toString();
      }
    });
    _createdResources.add(resourceID);
    new FilingVersionsApi(_client).createFilingVersion(filingVersionId);
    new FactsApi(_client).createFacts(filingVersionId, facts());
    return filingVersionId;
  }

  private List<Fact> facts() {
    final List<Fact> facts = Lists.newArrayList();
    for (int i = 0; i < FACT_COUNT; ++i) {
      final NumericFact fact = new NumericFact();
      fact.setType(TypeEnum.NUMERICFACT);
      fact.setId((long) i);
      fact.setEntity(new Entity().scheme("scheme").identifier("identifier"));
      fact.setPeriod(new ForeverPeriod());
      fact.setConcept(new Concept().name("foo:Bar"));
      fact.setDimensionValues(emptyList());
      fact.setSourceInformations(emptyList());
      fact.setDecimals(new Decimals().infinite(true));
      fact.setUnit(new Unit().numerators(singletonList(new Measure().name("iso4217:GBP"))));

      final String value = getValue(i);
      fact.setReportedValue(value);
      fact.setNumericValue(Double.valueOf(value));

      facts.add(fact);
    }
    return facts;
  }

  protected abstract String getValue(int i);

  @Override
  public void close() {
    for (final String resourceID : _createdResources) {
      try {
        _protectedResources.delete(resourceID);
      }
      catch (final Exception e) {
        // Ignore - this fails but hopefully won't in the future.
      }
    }
  }

}
