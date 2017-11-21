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

import static com.corefiling.labs.digit.service.impl.FilingInserter.FACT_COUNT;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.both;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.junit.Test;

import com.corefiling.labs.digit.test.AbstractApiClientIntegrationTest;
import com.corefiling.labs.digitFrequencyAnalysisService.AnalysisApi;
import com.corefiling.labs.digitFrequencyAnalysisService.model.AnalysisResponse;
import com.corefiling.labs.digitFrequencyAnalysisService.model.AnalysisResponseDigit;
import com.corefiling.labs.digitFrequencyAnalysisService.model.AnalysisResponseDigitProportion;
import com.corefiling.labs.digitFrequencyAnalysisService.model.AnalysisResponseDigitProportionExpectedPercentiles;
import com.corefiling.platform.instanceService.ApiClient;
import com.google.common.base.Preconditions;

/** Test /analyse. */
public class TestAnalysisApiIntegration extends AbstractApiClientIntegrationTest {

  private final class RealisticExpectedValueMatcher extends TypeSafeDiagnosingMatcher<List<AnalysisResponseDigitProportionExpectedPercentiles>> {
    @Override
    public void describeTo(final Description description) {
      description.appendText("realistic expectations with the value between the bounds; the bounds are not too wide; and the bounds are the same size");
    }
    @Override
    protected boolean matchesSafely(final List<AnalysisResponseDigitProportionExpectedPercentiles> expected, final Description mismatchDescription) {
      final double percentile1 = expected.get(0).getValue();
      final double percentile5 = expected.get(1).getValue();
      final double expectedValue = expected.get(2).getValue();
      final double percentile95 = expected.get(3).getValue();
      final double percentile99 = expected.get(4).getValue();

      boolean mismatch = false;
      if (percentile5 < percentile1) {
        mismatchDescription.appendText("5th percentile value " + percentile5 + " is lower than the 1st percentile value " + percentile1);
        mismatch = true;
      }
      if (expectedValue < percentile5) {
        mismatchDescription.appendText((mismatch ? " & " : "") + "expected value " + expectedValue + " is lower than the 5th percentile " + percentile5);
        mismatch = true;
      }
      if (percentile95 < expectedValue) {
        mismatchDescription.appendText((mismatch ? " & " : "") + "expected value " + expectedValue + " is greater than the 95th percentile value " + percentile95);
        mismatch = true;
      }
      if (percentile99 < percentile95) {
        mismatchDescription.appendText((mismatch ? " & " : "") + "95th percentile value " + percentile95 + " is greater than the 99th percentile value " + percentile99);
        mismatch = true;
      }
      if (expectedValue - percentile1 > 15) {
        mismatchDescription.appendText((mismatch ? " & " : "") + "1st percentile range [" + percentile1 + "," + expectedValue + "] is too large");
        mismatch = true;
      }
      if (percentile99 - expectedValue > 15) {
        mismatchDescription.appendText((mismatch ? " & " : "") + "99th percentile range [" + expectedValue + "," + percentile99 + "] is too large");
        mismatch = true;
      }
      if ((expectedValue - percentile5) - (percentile95 - expectedValue) > 0.05) {
        mismatchDescription.appendText((mismatch ? " & " : "") + "size of 5th percentile range [" + percentile5 + "," + expectedValue + "] does not match size of the 95th percentile range [" + expectedValue + "," + percentile95 + "]");
        mismatch = true;
      }
      if ((expectedValue - percentile1) - (percentile99 - expectedValue) > 0.05) {
        mismatchDescription.appendText((mismatch ? " & " : "") + "size of 1st percentile range [" + percentile1 + "," + expectedValue + "] does not match size of the 99th percentile range [" + expectedValue + "," + percentile99 + "]");
        mismatch = true;
      }
      return !mismatch;
    }
  }

  private final ApiClient _instanceServiceClient = createAuthenticatedInstanceServiceClient();
  private final AnalysisApi _analysisApi = new AnalysisApi(createAuthenticatedClient());
  private final KeycloakProtectedResources _protectedResources = new KeycloakProtectedResources(getAuthz());

  @Test
  public void testRandomlyGeneratedInput() throws Exception {
    final Random random = new Random(123456789L);
    try (final FilingInserter inserter = new FilingInserter(_instanceServiceClient, _protectedResources) {
      @Override
      protected String getValue(final int i) {
        return String.valueOf(random.nextDouble() * 10000 + 10);
      }
    }) {
      final UUID filingVersionId = inserter.insert();
      final AnalysisResponse response = _analysisApi.getDigitFrequency(filingVersionId);
      assertThat(response.getAnalysedFactCount().intValue(), equalTo(FACT_COUNT));
      assertThat(response.getChiSquared(), greaterThan(15.5));
      assertThat(response.getMeanAbsoluteDeviation(), greaterThan(0.05));

      final List<AnalysisResponseDigit> digits = response.getDigits();
      assertThat(digits.stream().map(d -> d.getDigit()).collect(toList()), contains(1, 2, 3, 4, 5, 6, 7, 8, 9));
      assertThat(digits.stream().map(d -> d.getProportion().getActualValue()).collect(toList()), everyItem(both(greaterThan(0.06)).and(lessThan(0.16))));
      assertThat(digits.stream().filter(d -> d.getDigit() != 3).map(d -> d.getProportion().getZTest()).collect(toList()), everyItem(greaterThan(1.0)));
      assertThat(digits.stream().map(d -> d.getProportion().getExpectedPercentiles()).collect(toList()), everyItem(new RealisticExpectedValueMatcher()));
    }
  }

  @Test
  public void testDataThatFollowsBenfordsLaw() throws Exception {
    try (final FilingInserter inserter = new FilingInserter(_instanceServiceClient, _protectedResources) {
      @Override
      protected String getValue(final int i) {
        return String.valueOf(getValueAsInteger((int) (i * (100.0 / FACT_COUNT))));
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
      final AnalysisResponse response = _analysisApi.getDigitFrequency(filingVersionId);
      assertThat(response.getAnalysedFactCount().intValue(), equalTo(FACT_COUNT));
      assertThat(response.getChiSquared(), lessThan(15.5));
      assertThat(response.getMeanAbsoluteDeviation(), lessThan(0.05));

      final List<AnalysisResponseDigit> digits = response.getDigits();
      assertThat(digits.stream().map(d -> d.getDigit()).collect(toList()), contains(1, 2, 3, 4, 5, 6, 7, 8, 9));
      assertThat(digits.stream().filter(d -> d.getDigit() != 3).map(d -> d.getProportion().getZTest()).collect(toList()), everyItem(lessThan(1.0)));
      assertThat(digits.stream().map(d -> d.getProportion().getExpectedPercentiles()).collect(toList()), everyItem(new RealisticExpectedValueMatcher()));
      assertThat(digits.stream().map(d -> d.getProportion()).collect(toList()), everyItem(new TypeSafeDiagnosingMatcher<AnalysisResponseDigitProportion>() {
        @Override
        public void describeTo(final Description description) {
          description.appendText("expected value and actual value to be approximately equal");
        }
        @Override
        protected boolean matchesSafely(final AnalysisResponseDigitProportion item, final Description mismatchDescription) {
          final double actualValue = item.getActualValue();
          final double expectedValue = item.getExpectedPercentiles().stream()
              .filter(p -> p.getPercentile() == 50.0)
              .map(p -> p.getValue())
              .reduce((d1, d2) -> { throw new AssertionError("Only one value should be present for 50th percentile."); })
              .get();
          if (Math.abs(actualValue - expectedValue) > 0.5) {
            mismatchDescription.appendText("actual value " + actualValue + " is not approximately equal to expected value " + expectedValue);
            return false;
          }
          return true;
        }
      }));
    }
  }

  @Test
  public void testNoRelevantData() throws Exception {
    try (FilingInserter inserter = new FilingInserter(_instanceServiceClient, _protectedResources) {
      @Override
      protected String getValue(final int i) {
        return "1";
      }
    }) {
      final UUID filingVersionId = inserter.insert();
      final AnalysisResponse response = _analysisApi.getDigitFrequency(filingVersionId);
      assertThat(response.getAnalysedFactCount().intValue(), equalTo(0));
      assertNull(response.getChiSquared());
      assertNull(response.getMeanAbsoluteDeviation());
      assertThat(response.getDigits(), empty()); // It's actually null but the client lies to us here.
    }
  }

  @Test
  public void testAcceptsUpperCaseISO4217() throws Exception {
    try (FilingInserter inserter = new FilingInserter(_instanceServiceClient, _protectedResources) {
      @Override
      protected String getNumerator() {
        return "ISO4217:USD";
      };
      @Override
      protected String getValue(final int i) {
        return "100";
      }
    }) {
      final UUID filingVersionId = inserter.insert();
      final AnalysisResponse response = _analysisApi.getDigitFrequency(filingVersionId);
      assertEquals(FACT_COUNT, response.getAnalysedFactCount().intValue());
      assertNotNull(response.getChiSquared());
      assertNotNull(response.getMeanAbsoluteDeviation());
      assertThat(response.getDigits(), hasSize(9));
    }
  }

}
