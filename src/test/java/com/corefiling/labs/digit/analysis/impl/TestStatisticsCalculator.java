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
