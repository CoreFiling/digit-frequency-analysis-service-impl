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
import com.corefiling.labs.digit.analysis.impl.StatisticsCalculator.DigitStatisticsCalculator;
import com.corefiling.labs.digit.model.AnalysisResponse;
import com.corefiling.labs.digit.model.DigitProportion;
import com.corefiling.labs.digit.model.DigitStatistics;
import com.corefiling.labs.digit.model.ExpectedDigitProportion;
import com.corefiling.labs.digit.model.ExpectedDigitProportionBounds;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

/** Analyses the digits in fact values. */
public class DigitAnalyserImpl implements DigitAnalyser {

  private static final AnalysisResponse NO_FACTS_RESPONSE = new AnalysisResponse().setAnalysedFactCount(0);

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

    final StatisticsCalculator statisticsCalculator = new StatisticsCalculator(totalCount);

    for (int digit = 1; digit <= NUMBER_OF_DIGITS; digit++) {
      final long countObserved = Optional.ofNullable(digitToCount.get(digit)).orElse(0L);
      final DigitStatisticsCalculator calculation = statisticsCalculator.forDigit(digit, countObserved);
      digits.add(createDigit(digit, calculation));
      chiSquared += calculation.getSquaredDifferenceOverExpected();
      sumAbsoluteDeviation += calculation.getAbsoluteProbabilityDeviation();
    }

    return createResponse(rawValues.size(), chiSquared, sumAbsoluteDeviation / NUMBER_OF_DIGITS, digits);
  }

  @VisibleForTesting static int toFirstDigit(final String value) {
    final BigDecimal decimalValue = new BigDecimal(value);
    final BigDecimal scaled = decimalValue.movePointLeft(decimalValue.precision() - decimalValue.scale() - 1);
    final int leadingDigit = Math.abs(scaled.byteValue());
    return leadingDigit;
  }

  private DigitStatistics createDigit(final int digit, final DigitStatisticsCalculator digitCalculator) {
    return new DigitStatistics()
        .setDigit(digit)
        .setProportion(new DigitProportion()
            .setActualValue(digitCalculator.getProbabilityObserved())
            .setZTest(digitCalculator.getZTest())
            .setExpected(new ExpectedDigitProportion()
                .setValue(digitCalculator.getPercentile(0))
                .setBounds(new ExpectedDigitProportionBounds()
                    .setLower(digitCalculator.getPercentile(-2.57))
                    .setUpper(digitCalculator.getPercentile(2.57))
                    )
                )
            );
  }

  private AnalysisResponse createResponse(final int factsAnalysed, final double chiSquared, final double meanAbsoluteDeviation, final List<DigitStatistics> digits) {
    return new AnalysisResponse()
        .setAnalysedFactCount(factsAnalysed)
        .setChiSquared(chiSquared)
        .setMeanAbsoluteDeviation(meanAbsoluteDeviation)
        .setDigits(digits);
  }

}
