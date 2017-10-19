package com.corefiling.labs.digit.model;

/** Represents the expected percentage bounds for a single digit. */
public class ExpectedDigitProportionBounds {

  private double _lower;
  private double _upper;

  public void setLower(final double lower) {
    _lower = lower;
  }

  public double getLower() {
    return _lower;
  }

  public void setUpper(final double upper) {
    _upper = upper;
  }

  public double getUpper() {
    return _upper;
  }

}
