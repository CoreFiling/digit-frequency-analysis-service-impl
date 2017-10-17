package com.corefiling.labs.service.impl;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertThat;

import java.util.UUID;

import org.junit.Test;

import com.corefiling.labs.digitFrequencyAnalysisService.AnalysisApi;
import com.corefiling.labs.digitFrequencyAnalysisService.model.AnalysisResponse;
import com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.AbstractApiClientIntegrationTest;

/** Test /analyse. */
public class TestAnalysisApiIntegration extends AbstractApiClientIntegrationTest {

  @Test
  public void testRandomlyGeneratedInput() throws Exception {
    final UUID filingVersionId = new FilingInserter() {
      @Override
      protected String getValue(final int i) {
        return String.valueOf(Math.random() * 10000);
      }
    }.insert();

    final AnalysisResponse response = new AnalysisApi(noRolesUser()).analyseFiling(filingVersionId);
    assertThat(response.getChiSquared(), greaterThan(15.5));
    assertThat(response.getMeanAbsoluteDeviation(), greaterThan(0.05));
  }

  @Test
  public void testDataThatFollowsBenfordsLaw() throws Exception {
    final UUID filingVersionId = new FilingInserter() {
      @Override
      protected String getValue(final int i) {
        return String.valueOf(getValueAsInteger((int) (i / 2.0)));
      }
      private int getValueAsInteger(final int i) {
        final int lessThanOneHundred = makeSmallEnough(i);
        if (i < 30) {
          return 100 + lessThanOneHundred;
        }
        if (i < 48) {
          return 200 + lessThanOneHundred;
        }
        if (i < 60) {
          return 300 + lessThanOneHundred;
        }
        if (i < 70) {
          return 400 + lessThanOneHundred;
        }
        if (i < 78) {
          return 500 + lessThanOneHundred;
        }
        if (i < 85) {
          return 600 + lessThanOneHundred;
        }
        if (i < 91) {
          return 700 + lessThanOneHundred;
        }
        if (i < 96) {
          return 800 + lessThanOneHundred;
        }
        return 900 + lessThanOneHundred;
      }
      private int makeSmallEnough(final int i) {
        return i < 100 ? i : makeSmallEnough(i - 20);
      }
    }.insert();

    final AnalysisResponse response = new AnalysisApi(noRolesUser()).analyseFiling(filingVersionId);
    assertThat(response.getChiSquared(), lessThan(15.5));
    assertThat(response.getMeanAbsoluteDeviation(), lessThan(0.05));
  }

}
