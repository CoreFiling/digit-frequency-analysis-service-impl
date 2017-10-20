package com.corefiling.labs.digit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Represents the percentages for a single digit. */
public class DigitProportion {

  private double _actualValue;
  private double _zTest;
  private ExpectedDigitProportion _expected;

  public void setActualValue(final double actualValue) {
    _actualValue = actualValue;
  }

  public double getActualValue() {
    return _actualValue;
  }

  public void setZTest(final double zTest) {
    _zTest = zTest;
  }

  @JsonProperty("zTest")
  public double getZTest() {
    return _zTest;
  }

  public void setExpected(final ExpectedDigitProportion expected) {
    _expected = expected;
  }

  public ExpectedDigitProportion getExpected() {
    return _expected;
  }

}
