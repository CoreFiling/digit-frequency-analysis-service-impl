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

import static java.util.Collections.emptyList;
import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.corefiling.labs.digit.analysis.NumericFactValue;
import com.corefiling.labs.digit.model.AnalysisResponse;
import com.corefiling.labs.digit.model.DigitPercentileProportionValue;
import com.corefiling.labs.digit.model.DigitProportion;
import com.corefiling.labs.digit.model.DigitStatistics;
import com.google.common.collect.ImmutableList;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

/** Test {@link DigitAnalyserImpl}. */
@RunWith(JUnitParamsRunner.class)
public class TestDigitAnalyserImpl {

  private static class SimpleNumericFactValue implements NumericFactValue {

    private final boolean _isMonetary;
    private final String _value;

    public SimpleNumericFactValue(final boolean isMonetary, final String value) {
      _isMonetary = isMonetary;
      _value = value;
    }

    @Override
    public boolean isMonetary() {
      return _isMonetary;
    }

    @Override
    public boolean hasValue() {
      return _value != null;
    }

    @Override
    public String getValue() {
      return _value;
    }
  }

  @Test
  @Parameters({
    "1, 1", "4, 4", "0, 0",
    "84, 8", "123, 1", "987, 9", "666, 6",
    "0.6, 6", "0.65, 6", "0.7, 7", "0.8, 8",
    "0.00004, 4",
    "12.34, 1"
  })
  public void testToFirstDigit(final String input, final int expected) {
    assertEquals(expected, DigitAnalyserImpl.toFirstDigit(input));
  }

  @Test @Parameters(method = "paramsEmptyResponse")
  public void testEmptyResponse(final List<NumericFactValue> facts) {
    final AnalysisResponse response = new DigitAnalyserImpl().analyse(facts);
    assertEquals(0, response.getAnalysedFactCount().intValue());
    assertNull(response.getChiSquared());
    assertNull(response.getMeanAbsoluteDeviation());
    assertNull(response.getDigits());
  }

  @SuppressWarnings("unused")
  private List<Object[]> paramsEmptyResponse() {
    return Stream.of(
        emptyList(), // No facts
        nCopies(200, new SimpleNumericFactValue(false, "123")), // Not monetary
        nCopies(200, new SimpleNumericFactValue(true, null)), // No value
        nCopies(200, new SimpleNumericFactValue(true, "-100")), // Negative facts are discarded
        nCopies(200, new SimpleNumericFactValue(true, "5")) // Small values are discarded
        )
        .map(list -> new Object[] {list})
        .collect(toList());
  }

  @Test
  public void testRepeatedMonetaryValue() {
    final AnalysisResponse response = new DigitAnalyserImpl().analyse(nCopies(200, new SimpleNumericFactValue(true, "123")));
    assertEquals(200, response.getAnalysedFactCount().intValue());
    assertEquals(464.4, response.getChiSquared(), 0.05);
    assertEquals(0.155, response.getMeanAbsoluteDeviation(), 0.0005);

    final List<DigitStatistics> digits = response.getDigits();
    assertThat(digits, hasSize(9));
    assertDigit(digits.get(0), 1, 1, 21.47, 0.22, 0.23, 0.30, 0.37, 0.39);
    assertDigit(digits.get(1), 2, 0, 6.45, 0.10, 0.12, 0.18, 0.23, 0.25);
    assertDigit(digits.get(2), 3, 0, 5.24, 0.06, 0.08, 0.12, 0.17, 0.19);
    assertDigit(digits.get(3), 4, 0, 4.51, 0.04, 0.05, 0.10, 0.14, 0.15);
    assertDigit(digits.get(4), 5, 0, 4.02, 0.03, 0.04, 0.08, 0.12, 0.13);
    assertDigit(digits.get(5), 6, 0, 3.65, 0.02, 0.03, 0.07, 0.10, 0.11);
    assertDigit(digits.get(6), 7, 0, 3.36, 0.01, 0.02, 0.06, 0.09, 0.10);
    assertDigit(digits.get(7), 8, 0, 3.12, 0.01, 0.02, 0.05, 0.08, 0.09);
    assertDigit(digits.get(8), 9, 0, 2.93, 0.01, 0.01, 0.05, 0.08, 0.09);
  }

  @Test
  public void testBenfordyData() {
    final List<NumericFactValue> firstDigit = nCopies(30, new SimpleNumericFactValue(true, "100"));
    final List<NumericFactValue> secondDigit = nCopies(18, new SimpleNumericFactValue(true, "200"));
    final List<NumericFactValue> thirdDigit = nCopies(12, new SimpleNumericFactValue(true, "300"));
    final List<NumericFactValue> fourDigit = nCopies(10, new SimpleNumericFactValue(true, "400"));
    final List<NumericFactValue> fiveDigit = nCopies(8, new SimpleNumericFactValue(true, "500"));
    final List<NumericFactValue> sixDigit = nCopies(7, new SimpleNumericFactValue(true, "600"));
    final List<NumericFactValue> sevenDigit = nCopies(6, new SimpleNumericFactValue(true, "700"));
    final List<NumericFactValue> eightDigit = nCopies(5, new SimpleNumericFactValue(true, "800"));
    final List<NumericFactValue> nineDigit = nCopies(5, new SimpleNumericFactValue(true, "900"));

    final List<NumericFactValue> values = ImmutableList.<NumericFactValue>builder()
        .addAll(firstDigit)
        .addAll(secondDigit)
        .addAll(thirdDigit)
        .addAll(fourDigit)
        .addAll(fiveDigit)
        .addAll(sixDigit)
        .addAll(sevenDigit)
        .addAll(eightDigit)
        .addAll(nineDigit)
        .build();

    final AnalysisResponse response = new DigitAnalyserImpl().analyse(values);
    assertEquals(101, response.getAnalysedFactCount().intValue());
    assertEquals(0.091, response.getChiSquared(), 0.0005);
    assertEquals(0.00262, response.getMeanAbsoluteDeviation(), 0.000005);

    final List<DigitStatistics> digits = response.getDigits();
    assertThat(digits, hasSize(9));
    assertDigit(digits.get(0), 1, 0.30, 0.09, 0.18, 0.21, 0.30, 0.40, 0.42);
    assertDigit(digits.get(1), 2, 0.18, 0.06, 0.07, 0.10, 0.18, 0.26, 0.28);
    assertDigit(digits.get(2), 3, 0.12, 0.04, 0.04, 0.06, 0.12, 0.19, 0.21);
    assertDigit(digits.get(3), 4, 0.10, 0.07, 0.02, 0.03, 0.10, 0.16, 0.18);
    assertDigit(digits.get(4), 5, 0.08, 0.00, 0.01, 0.02, 0.08, 0.14, 0.15);
    assertDigit(digits.get(5), 6, 0.07, 0.09, 0.00, 0.01, 0.07, 0.12, 0.14);
    assertDigit(digits.get(6), 7, 0.06, 0.06, 0.00, 0.01, 0.06, 0.11, 0.12);
    assertDigit(digits.get(7), 8, 0.05, 0.08, 0.00, 0.00, 0.05, 0.10, 0.11);
    assertDigit(digits.get(8), 9, 0.05, 0.18, 0.00, 0.00, 0.05, 0.09, 0.10);
  }

  // CSOFF: ParameterNumber
  private void assertDigit(final DigitStatistics digitStatistics, final int digit, final double actualProportion, final double zTest,
      final double percentile1, final double percentile5, final double percentile50, final double percentile95, final double percentile99) {
  // CSON: ParameterNumber
    assertEquals("Digit", digit, digitStatistics.getDigit());
    final DigitProportion digitProportion = digitStatistics.getProportion();
    assertEquals("Actual value", actualProportion, digitProportion.getActualValue(), 0.005);
    assertEquals("Z Test", zTest, digitProportion.getZTest(), 0.005);

    final List<DigitPercentileProportionValue> percentiles = digitProportion.getExpectedPercentiles();
    assertThat(percentiles.get(0), allOf(hasProperty("percentile", equalTo(1.0)), hasProperty("value", closeTo(percentile1, 0.005))));
    assertThat(percentiles.get(1), allOf(hasProperty("percentile", equalTo(5.0)), hasProperty("value", closeTo(percentile5, 0.005))));
    assertThat(percentiles.get(2), allOf(hasProperty("percentile", equalTo(50.0)), hasProperty("value", closeTo(percentile50, 0.005))));
    assertThat(percentiles.get(3), allOf(hasProperty("percentile", equalTo(95.0)), hasProperty("value", closeTo(percentile95, 0.005))));
    assertThat(percentiles.get(4), allOf(hasProperty("percentile", equalTo(99.0)), hasProperty("value", closeTo(percentile99, 0.005))));
  }

}
