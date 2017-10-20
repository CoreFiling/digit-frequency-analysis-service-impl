package com.corefiling.labs.digit.analysis.impl;

import static java.util.Collections.emptyList;
import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.toList;
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
import com.corefiling.labs.digit.model.DigitProportion;
import com.corefiling.labs.digit.model.DigitStatistics;
import com.corefiling.labs.digit.model.ExpectedDigitProportion;
import com.corefiling.labs.digit.model.ExpectedDigitProportionBounds;
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
    assertDigit(digits.get(0), 1, 1, 0.30, 0.22, 0.39, 21.47);
    assertDigit(digits.get(1), 2, 0, 0.18, 0.11, 0.25, 6.45);
    assertDigit(digits.get(2), 3, 0, 0.12, 0.07, 0.19, 5.24);
    assertDigit(digits.get(3), 4, 0, 0.10, 0.05, 0.15, 4.51);
    assertDigit(digits.get(4), 5, 0, 0.08, 0.03, 0.13, 4.02);
    assertDigit(digits.get(5), 6, 0, 0.07, 0.02, 0.11, 3.65);
    assertDigit(digits.get(6), 7, 0, 0.06, 0.02, 0.10, 3.36);
    assertDigit(digits.get(7), 8, 0, 0.05, 0.01, 0.09, 3.12);
    assertDigit(digits.get(8), 9, 0, 0.05, 0.01, 0.09, 2.93);
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
    assertDigit(digits.get(0), 1, 0.30, 0.30, 0.19, 0.42, 0.09);
    assertDigit(digits.get(1), 2, 0.18, 0.18, 0.08, 0.28, 0.06);
    assertDigit(digits.get(2), 3, 0.12, 0.12, 0.05, 0.21, 0.04);
    assertDigit(digits.get(3), 4, 0.10, 0.10, 0.03, 0.18, 0.07);
    assertDigit(digits.get(4), 5, 0.08, 0.08, 0.02, 0.15, 0.00);
    assertDigit(digits.get(5), 6, 0.07, 0.07, 0.01, 0.14, 0.09);
    assertDigit(digits.get(6), 7, 0.06, 0.06, 0.00, 0.12, 0.06);
    assertDigit(digits.get(7), 8, 0.05, 0.05, 0.00, 0.11, 0.08);
    assertDigit(digits.get(8), 9, 0.05, 0.05, 0.00, 0.10, 0.18);
  }

  private void assertDigit(final DigitStatistics digitStatistics, final int digit, final double actualProportion, final double expectedProportion, final double expectedLowerBound, final double expectedUpperBound, final double zTest) {
    assertEquals(digit, digitStatistics.getDigit());
    final DigitProportion digitProportion = digitStatistics.getProportion();
    assertEquals(actualProportion, digitProportion.getActualValue(), 0.005);
    assertEquals(zTest, digitProportion.getZTest(), 0.005);
    final ExpectedDigitProportion expectedDigitProportion = digitProportion.getExpected();
    assertEquals(expectedProportion, expectedDigitProportion.getValue(), 0.005);
    final ExpectedDigitProportionBounds expectedDigitProportionBounds = expectedDigitProportion.getBounds();
    assertEquals(expectedLowerBound, expectedDigitProportionBounds.getLower(), 0.005);
    assertEquals(expectedUpperBound, expectedDigitProportionBounds.getUpper(), 0.005);
  }

}
