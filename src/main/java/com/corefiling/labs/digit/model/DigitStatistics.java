package com.corefiling.labs.digit.model;

/** Represents the statistics for a single digit. */
public class DigitStatistics {

  private int _digit;
  private DigitProportion _proportion;

  public void setDigit(final int digit) {
    _digit = digit;
  }

  public int getDigit() {
    return _digit;
  }

  public void setProportion(final DigitProportion proportion) {
    _proportion = proportion;
  }

  public DigitProportion getProportion() {
    return _proportion;
  }

}
