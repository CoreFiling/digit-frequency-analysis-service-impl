package com.corefiling.labs.digit.analysis.impl;

import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.corefiling.labs.digit.analysis.DigitAnalyser;
import com.corefiling.labs.digit.analysis.NumericFactValue;
import com.corefiling.labs.digit.model.AnalysisResponse;
import com.corefiling.labs.digit.model.DigitProportion;
import com.corefiling.labs.digit.model.DigitStatistics;
import com.corefiling.labs.digit.model.ExpectedDigitProportion;
import com.corefiling.labs.digit.model.ExpectedDigitProportionBounds;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

/** Analyses the digits in fact values. */
public class DigitAnalyserImpl implements DigitAnalyser {

  private static final AnalysisResponse NO_FACTS_RESPONSE = new AnalysisResponse().analysedFactCount(0);

  private static final int NUMBER_OF_DIGITS = 9;

  @Override
  public AnalysisResponse analyse(final List<NumericFactValue> facts) {
    final List<String> rawValues = facts.stream()
        .filter(f -> f.isMonetary() && f.hasValue())
        .map(f -> f.getValue())
        .filter(value -> Double.parseDouble(value) > 10) // Small & negative values may skew the results.
        .collect(toList());

    if (rawValues.isEmpty()) {
      return NO_FACTS_RESPONSE;
    }

    final Map<Integer, Long> digitToCount = rawValues.stream().collect(groupingBy(DigitAnalyserImpl::toFirstDigit, counting()));
    final double totalCount = digitToCount.values().stream().mapToLong(l -> l).sum();

    double chiSquared = 0;
    double sumAbsoluteDeviation = 0;
    final List<DigitStatistics> digits = Lists.newArrayList();

    final double oneOver2N = 0.5d / totalCount;
    for (int digit = 1; digit <= NUMBER_OF_DIGITS; digit++) {
      final long countObserved = Optional.ofNullable(digitToCount.get(digit)).orElse(0L);
      final double probabilityObserved = countObserved / totalCount;
      final double probabilityExpected = Math.log10(1d + 1d / digit);
      final double countExpected = probabilityExpected * totalCount;

      final double absProbabilityDifference = Math.abs(probabilityObserved - probabilityExpected);
      final double standardDeviation = Math.sqrt((probabilityExpected * (1d - probabilityExpected)) / totalCount);

      final double lowerBound = probabilityExpected - (2.57 * standardDeviation - oneOver2N);
      final double upperBound = probabilityExpected + (2.57 * standardDeviation + oneOver2N);

      final boolean shouldApplyContinuityCorrection = absProbabilityDifference > oneOver2N;
      final double zStat = shouldApplyContinuityCorrection ? (absProbabilityDifference - oneOver2N) / standardDeviation : absProbabilityDifference / standardDeviation;

      digits.add(createDigit(digit, probabilityExpected, lowerBound, upperBound, probabilityObserved, zStat));

      chiSquared += Math.pow(countObserved - countExpected, 2) / countExpected;
      sumAbsoluteDeviation += absProbabilityDifference;
    }

    return createResponse(rawValues.size(), chiSquared, sumAbsoluteDeviation / NUMBER_OF_DIGITS, digits);
  }

  @VisibleForTesting static int toFirstDigit(final String value) {
    final BigDecimal decimalValue = new BigDecimal(value);
    final BigDecimal scaled = decimalValue.movePointLeft(decimalValue.precision() - decimalValue.scale() - 1);
    final int leadingDigit = Math.abs(scaled.byteValue());
    return leadingDigit;
  }

  private DigitStatistics createDigit(final int digit, final double probabilityExpected, final double lowerBound, final double upperBound, final double probabilityObserved, final double zStat) {
    final DigitStatistics digitStats = new DigitStatistics();
    digitStats.setDigit(digit);
    final DigitProportion proportion = new DigitProportion();
    proportion.setActualValue(probabilityObserved);
    proportion.setZTest(zStat);
    final ExpectedDigitProportion expected = new ExpectedDigitProportion();
    expected.setValue(probabilityExpected);
    final ExpectedDigitProportionBounds expectedBounds = new ExpectedDigitProportionBounds();
    expectedBounds.setLower(lowerBound);
    expectedBounds.setUpper(upperBound);
    expected.setBounds(expectedBounds);
    proportion.setExpected(expected);
    digitStats.setProportion(proportion);
    return digitStats;
  }

  private AnalysisResponse createResponse(final int factsAnalysed, final double chiSquared, final double meanAbsoluteDeviation, final List<DigitStatistics> digits) {
    final AnalysisResponse response = new AnalysisResponse();
    response.setAnalysedFactCount(factsAnalysed);
    response.setChiSquared(chiSquared);
    response.setMeanAbsoluteDeviation(meanAbsoluteDeviation);
    response.setDigits(digits);
    return response;
  }

}
