package com.corefiling.labs.digit.model;

/** Represents the expected percentages for a single digit. */
public class ExpectedDigitProportion {

  private double _value;
  private ExpectedDigitProportionBounds _bounds;

  public ExpectedDigitProportion setValue(final double value) {
    _value = value;
    return this;
  }

  public double getValue() {
    return _value;
  }

  public ExpectedDigitProportion setBounds(final ExpectedDigitProportionBounds bounds) {
    _bounds = bounds;
    return this;
  }

  public ExpectedDigitProportionBounds getBounds() {
    return _bounds;
  }

}
