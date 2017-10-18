package com.corefiling.labs.analysis.impl;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.corefiling.labs.analysis.DigitAnalyser;
import com.corefiling.labs.analysis.NumericFactValue;
import com.corefiling.labs.model.AnalysisResponse;
import com.google.common.annotations.VisibleForTesting;

/** Analyses the digits in fact values. */
public class DigitAnalyserImpl implements DigitAnalyser {

  private static final AnalysisResponse EMPTY_RESPONSE = new AnalysisResponse();

  private static final int NUMBER_OF_DIGITS = 9;

  @Override
  public AnalysisResponse analyse(final List<NumericFactValue> facts) {
    final List<String> rawValues = facts.stream()
        .filter(f -> f.isMonetary() && f.hasValue())
        .map(f -> f.getValue())
        .filter(value -> Double.parseDouble(value) > 10) // Small & negative values may skew the results.
        .collect(toList());

    if (rawValues.size() < 150) {
      return EMPTY_RESPONSE;
    }

    final Map<Integer, Long> digitToCount = rawValues.stream().collect(groupingBy(DigitAnalyserImpl::toFirstDigit, counting()));
    final double totalCount = digitToCount.values().stream().mapToLong(l -> l).sum();

    double chiSquared = 0;
    double sumAbsoluteDeviation = 0;

    for (int digit = 1; digit <= NUMBER_OF_DIGITS; digit++) {
      final long countObserved = Optional.ofNullable(digitToCount.get(digit)).orElse(0L);
      final double probabilityObserved = countObserved / totalCount;
      final double probabilityExpected = Math.log10(1d + 1d / digit);
      final double countExpected = probabilityExpected * totalCount;
      final double absProbabilityDifference = Math.abs(probabilityObserved - probabilityExpected);

      chiSquared += Math.pow(countObserved - countExpected, 2) / countExpected;
      sumAbsoluteDeviation += absProbabilityDifference;
    }

    return createResponse(chiSquared, sumAbsoluteDeviation / NUMBER_OF_DIGITS);
  }

  @VisibleForTesting static int toFirstDigit(final String value) {
    final BigDecimal decimalValue = new BigDecimal(value);
    final BigDecimal scaled = decimalValue.movePointLeft(decimalValue.precision() - decimalValue.scale() - 1);
    final int leadingDigit = Math.abs(scaled.byteValue());
    return leadingDigit;
  }

  private AnalysisResponse createResponse(final double chiSquared, final double meanAbsoluteDeviation) {
    final AnalysisResponse response = new AnalysisResponse();
    response.setChiSquared(chiSquared);
    response.setMeanAbsoluteDeviation(meanAbsoluteDeviation);
    return response;
  }

}
