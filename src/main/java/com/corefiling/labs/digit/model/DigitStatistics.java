package com.corefiling.labs.digit.model;

/** Represents the statistics for a single digit. */
public class DigitStatistics {

  private int _digit;
  private DigitProportion _proportion;

  public DigitStatistics setDigit(final int digit) {
    _digit = digit;
    return this;
  }

  public int getDigit() {
    return _digit;
  }

  public DigitStatistics setProportion(final DigitProportion proportion) {
    _proportion = proportion;
    return this;
  }

  public DigitProportion getProportion() {
    return _proportion;
  }

}
