package com.corefiling.labs.analysis.impl;

import static java.util.Collections.emptyList;
import static java.util.Collections.nCopies;
import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;
import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.corefiling.labs.analysis.NumericFactValue;
import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.model.AnalysisResponse;
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
    assertEquals(new AnalysisResponse(), response);
    assertNull(response.getChiSquared());
    assertNull(response.getMeanAbsoluteDeviation());
  }

  @SuppressWarnings("unused")
  private List<Object[]> paramsEmptyResponse() {
    return Stream.of(
        emptyList(), // No facts
        nCopies(200, new SimpleNumericFactValue(false, "123")), // Not monetary
        nCopies(200, new SimpleNumericFactValue(true, null)), // No value
        nCopies(100, new SimpleNumericFactValue(true, "123")), // Not enough data
        nCopies(200, new SimpleNumericFactValue(true, "-100")), // Negative facts are discarded
        nCopies(200, new SimpleNumericFactValue(true, "5")) // Small values are discarded
        )
        .map(list -> new Object[] {list})
        .collect(toList());
  }

  @Test
  public void testRepeatedMonetaryValue() {
    final AnalysisResponse response = new DigitAnalyserImpl().analyse(nCopies(200, new SimpleNumericFactValue(true, "123")));
    assertEquals(464.4, response.getChiSquared(), 0.05);
    assertEquals(0.155, response.getMeanAbsoluteDeviation(), 0.0005);
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
        .addAll(firstDigit).addAll(firstDigit)
        .addAll(secondDigit).addAll(secondDigit)
        .addAll(thirdDigit).addAll(thirdDigit)
        .addAll(fourDigit).addAll(fourDigit)
        .addAll(fiveDigit).addAll(fiveDigit)
        .addAll(sixDigit).addAll(sixDigit)
        .addAll(sevenDigit).addAll(sevenDigit)
        .addAll(eightDigit).addAll(eightDigit)
        .addAll(nineDigit).addAll(nineDigit)
        .build();

    final AnalysisResponse response = new DigitAnalyserImpl().analyse(values);
    assertEquals(0.182, response.getChiSquared(), 0.0005);
    assertEquals(0.00262, response.getMeanAbsoluteDeviation(), 0.000005);
  }

}
