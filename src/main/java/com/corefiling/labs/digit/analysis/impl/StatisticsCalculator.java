package com.corefiling.labs.digit.analysis.impl;

/** Calculates the statistic for each digit. */
public class StatisticsCalculator {

  private final double _totalCount;
  private final double _oneOver2N;

  public StatisticsCalculator(final double totalCount) {
    _totalCount = totalCount;
    _oneOver2N = 0.5 / totalCount;
  }

  public DigitStatisticsCalculator forDigit(final int digit, final long countObserved) {
    return new DigitStatisticsCalculator(countObserved / _totalCount, Math.log10(1d + 1d / digit));
  }

  /** Calculates the statistic for a single digit. */
  public class DigitStatisticsCalculator {

    private final double _probabilityObserved;
    private final double _probabilityExpected;
    private final double _standardDeviation;

    private DigitStatisticsCalculator(final double probabilityObserved, final double probabilityExpected) {
      _probabilityObserved = probabilityObserved;
      _probabilityExpected = probabilityExpected;
      _standardDeviation = Math.sqrt((_probabilityExpected * (1d - _probabilityExpected)) / _totalCount);
    }

    public double getProbabilityObserved() {
      return _probabilityObserved;
    }

    public double getProbabilityExpected() {
      return _probabilityExpected;
    }

    public double getAbsoluteProbabilityDeviation() {
      return Math.abs(_probabilityObserved - _probabilityExpected);
    }

    public double getSquaredDifferenceOverExpected() {
      return _totalCount * Math.pow(getAbsoluteProbabilityDeviation(), 2) / _probabilityExpected;
    }

    public double getZTest() {
      final double absProbabilityDifference = getAbsoluteProbabilityDeviation();
      final boolean shouldApplyContinuityCorrection = absProbabilityDifference > _oneOver2N;
      return shouldApplyContinuityCorrection ? (absProbabilityDifference - _oneOver2N) / _standardDeviation : absProbabilityDifference / _standardDeviation;
    }

    public double getLowerBound() {
      return _probabilityExpected - (2.57 * _standardDeviation - _oneOver2N);
    }

    public double getUpperBound() {
      return _probabilityExpected + (2.57 * _standardDeviation + _oneOver2N);
    }

  }

}
