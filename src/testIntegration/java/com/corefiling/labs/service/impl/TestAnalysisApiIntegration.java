package com.corefiling.labs.service.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.Random;
import java.util.UUID;

import org.junit.Test;

import com.corefiling.labs.digitFrequencyAnalysisService.AnalysisApi;
import com.corefiling.labs.digitFrequencyAnalysisService.model.AnalysisResponse;
import com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.AbstractApiClientIntegrationTest;
import com.google.common.base.Preconditions;

/** Test /analyse. */
public class TestAnalysisApiIntegration extends AbstractApiClientIntegrationTest {

  @Test
  public void testRandomlyGeneratedInput() throws Exception {
    final Random random = new Random(123456789L);
    final UUID filingVersionId = new FilingInserter(instanceServiceClient()) {
      @Override
      protected String getValue(final int i) {
        return String.valueOf(random.nextDouble() * 10000);
      }
    }.insert();

    final AnalysisResponse response = new AnalysisApi(getClient()).analyseFiling(filingVersionId);
    assertThat(response.getChiSquared(), greaterThan(15.5));
    assertThat(response.getMeanAbsoluteDeviation(), greaterThan(0.05));
  }

  @Test
  public void testDataThatFollowsBenfordsLaw() throws Exception {
    final UUID filingVersionId = new FilingInserter(instanceServiceClient()) {
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
    }.insert();

    final AnalysisResponse response = new AnalysisApi(getClient()).analyseFiling(filingVersionId);
    assertThat(response.getChiSquared(), lessThan(15.5));
    assertThat(response.getMeanAbsoluteDeviation(), lessThan(0.05));
  }

  @Test
  public void testNotEnoughData() throws Exception {
    final UUID filingVersionId = new FilingInserter(instanceServiceClient()) {
      @Override
      protected int getNumberOfFacts() {
        return 5;
      };
      @Override
      protected String getValue(final int i) {
        return "123";
      }
    }.insert();

    final AnalysisResponse response = new AnalysisApi(getClient()).analyseFiling(filingVersionId);
    assertNull(response.getChiSquared());
    assertNull(response.getMeanAbsoluteDeviation());
  }

}
