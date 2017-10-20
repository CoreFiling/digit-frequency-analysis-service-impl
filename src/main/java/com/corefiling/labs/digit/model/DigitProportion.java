package com.corefiling.labs.digit.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Represents the percentages for a single digit. */
public class DigitProportion {

  private double _actualValue;
  private double _zTest;
  private ExpectedDigitProportion _expected;

  public DigitProportion setActualValue(final double actualValue) {
    _actualValue = actualValue;
    return this;
  }

  public double getActualValue() {
    return _actualValue;
  }

  public DigitProportion setZTest(final double zTest) {
    _zTest = zTest;
    return this;
  }

  @JsonProperty("zTest")
  public double getZTest() {
    return _zTest;
  }

  public DigitProportion setExpected(final ExpectedDigitProportion expected) {
    _expected = expected;
    return this;
  }

  public ExpectedDigitProportion getExpected() {
    return _expected;
  }

}
