package com.corefiling.labs.digit.model;

/** Represents the expected percentages for a single digit. */
public class ExpectedDigitProportion {

  private double _value;
  private ExpectedDigitProportionBounds _bounds;

  public void setValue(final double value) {
    _value = value;
  }

  public double getValue() {
    return _value;
  }

  public void setBounds(final ExpectedDigitProportionBounds bounds) {
    _bounds = bounds;
  }

  public ExpectedDigitProportionBounds getBounds() {
    return _bounds;
  }

}
