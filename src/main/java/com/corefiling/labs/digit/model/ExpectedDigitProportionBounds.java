package com.corefiling.labs.digit.model;

/** Represents the expected percentage bounds for a single digit. */
public class ExpectedDigitProportionBounds {

  private double _lower;
  private double _upper;

  public ExpectedDigitProportionBounds setLower(final double lower) {
    _lower = lower;
    return this;
  }

  public double getLower() {
    return _lower;
  }

  public ExpectedDigitProportionBounds setUpper(final double upper) {
    _upper = upper;
    return this;
  }

  public double getUpper() {
    return _upper;
  }

}
