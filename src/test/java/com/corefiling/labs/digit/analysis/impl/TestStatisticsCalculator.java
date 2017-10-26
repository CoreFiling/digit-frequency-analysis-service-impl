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

import static org.hamcrest.Matchers.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import com.corefiling.labs.digit.analysis.impl.StatisticsCalculator.DigitStatisticsCalculator;

/** Test {@link StatisticsCalculator}. */
public class TestStatisticsCalculator {

  @Test
  public void testPercentagesAreAlwaysBetween0And1() {
    final StatisticsCalculator calculator = new StatisticsCalculator(50);
    final DigitStatisticsCalculator digitCalculator = calculator.forDigit(5, 0);
    assertEquals(0, digitCalculator.getPercentile(-100), 0.005);
    assertEquals(1, digitCalculator.getPercentile(100), 0.005);
  }

  @Test
  public void testZTestValueOf0GivesTheExpectedPercentage() {
    final StatisticsCalculator calculator = new StatisticsCalculator(50);
    for (int digit = 1; digit <= 9; ++digit) {
      final DigitStatisticsCalculator digitCalculator = calculator.forDigit(digit, 0);
      assertEquals(Math.log10(1d + 1d / digit), digitCalculator.getPercentile(0), 0.005);
    }
  }

  @Test
  public void testNegativeZTestValueIsOppositeOfPositiveZTestValue() {
    final StatisticsCalculator calculator = new StatisticsCalculator(50);
    final DigitStatisticsCalculator digitCalculator = calculator.forDigit(5, 0);
    final double upper = digitCalculator.getPercentile(1.5);
    final double expected = digitCalculator.getPercentile(0);
    final double lower = digitCalculator.getPercentile(-1.5);
    assertThat(upper, greaterThan(expected));
    assertThat(expected, greaterThan(lower));
    assertEquals(upper - expected, expected - lower, 0.005);
  }

}
