package com.corefiling.labs.digit.service.impl;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Random;
import java.util.UUID;

import org.junit.Test;

import com.corefiling.labs.digit.test.AbstractApiClientIntegrationTest;
import com.corefiling.labs.digitFrequencyAnalysisService.AnalysisApi;
import com.corefiling.labs.digitFrequencyAnalysisService.model.AnalysisResponse;
import com.corefiling.platform.instanceService.ApiClient;
import com.google.common.base.Preconditions;

/** Test /analyse. */
public class TestAnalysisApiIntegration extends AbstractApiClientIntegrationTest {

  private final ApiClient _instanceServiceClient = createAuthenticatedInstanceServiceClient();
  private final AnalysisApi _analysisApi = new AnalysisApi(createAuthenticatedClient());
  private final KeycloakProtectedResources _protectedResources = new KeycloakProtectedResources(getAuthz());

  @Test
  public void testRandomlyGeneratedInput() throws Exception {
    final Random random = new Random(123456789L);
    try (FilingInserter inserter = new FilingInserter(_instanceServiceClient, _protectedResources) {
      @Override
      protected String getValue(final int i) {
        return String.valueOf(random.nextDouble() * 10000);
      }
    }) {
      final UUID filingVersionId = inserter.insert();
      final AnalysisResponse response = _analysisApi.analyseFiling(filingVersionId);
      // Some of these will be less than 10 and excluded.
      assertThat(response.getAnalysedFactCount().intValue(), greaterThan(inserter.getNumberOfFacts() / 2));
      assertThat(response.getChiSquared(), greaterThan(15.5));
      assertThat(response.getMeanAbsoluteDeviation(), greaterThan(0.05));
    }
  }

  @Test
  public void testDataThatFollowsBenfordsLaw() throws Exception {
    try (final FilingInserter inserter = new FilingInserter(_instanceServiceClient, _protectedResources) {
      @Override
      protected String getValue(final int i) {
        return String.valueOf(getValueAsInteger((int) (i * (100.0 / getNumberOfFacts()))));
      }
      private int getValueAsInteger(final int i) {
        Preconditions.checkArgument(i <= 100);
        if (i < 30) {
          return 100 + i;
        }
        if (i < 48) {
          return 200 + i;
        }
        if (i < 60) {
          return 300 + i;
        }
        if (i < 70) {
          return 400 + i;
        }
        if (i < 78) {
          return 500 + i;
        }
        if (i < 85) {
          return 600 + i;
        }
        if (i < 91) {
          return 700 + i;
        }
        if (i < 96) {
          return 800 + i;
        }
        return 900 + i;
      }
    }) {
      final UUID filingVersionId = inserter.insert();
      final AnalysisResponse response = _analysisApi.analyseFiling(filingVersionId);
      assertThat(response.getAnalysedFactCount().intValue(), equalTo(inserter.getNumberOfFacts()));
      assertThat(response.getChiSquared(), lessThan(15.5));
      assertThat(response.getMeanAbsoluteDeviation(), lessThan(0.05));
    }
  }

  @Test
  public void testNoRelevantData() throws Exception {
    try (FilingInserter inserter = new FilingInserter(_instanceServiceClient, _protectedResources) {
      @Override
      public int getNumberOfFacts() {
        return 5;
      };
      @Override
      protected String getValue(final int i) {
        return "1";
      }
    }) {
      final UUID filingVersionId = inserter.insert();
      final AnalysisResponse response = _analysisApi.analyseFiling(filingVersionId);
      assertThat(response.getAnalysedFactCount().intValue(), equalTo(0));
      assertNull(response.getChiSquared());
      assertNull(response.getMeanAbsoluteDeviation());
    }
  }

}
