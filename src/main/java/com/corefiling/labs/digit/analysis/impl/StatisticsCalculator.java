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

    public double getPercentile(final double zThreshold) {
      final double percentile = _probabilityExpected + zThreshold * _standardDeviation;
      if (zThreshold < 0) {
        // We want to continuity correct down for the lower percentile because we care about the area above it
        final double correctedPercentile = percentile - _oneOver2N;
        // But we don't want to correct below 0.
        return correctedPercentile < 0 ? 0 : correctedPercentile;
      }
      else if (zThreshold == 0) {
        return percentile;
      }
      else {
        // We want to continuity correct up for the upper percentile because we are about the area below it.
        final double correctedPercentile = percentile + _oneOver2N;
        // But we don't want to correct over 1.
        return correctedPercentile > 1 ? 1 : correctedPercentile;
      }
    }

  }

}
