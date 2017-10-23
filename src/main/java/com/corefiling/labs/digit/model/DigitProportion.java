package com.corefiling.labs.digit.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/** Represents the percentages for a single digit. */
public class DigitProportion {

  private double _actualValue;
  private double _zTest;
  private List<DigitPercentileProportionValue> _percentiles;

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

  public DigitProportion setExpectedPercentiles(final List<DigitPercentileProportionValue> percentiles) {
    _percentiles = percentiles;
    return this;
  }

  public List<DigitPercentileProportionValue> getExpectedPercentiles() {
    return _percentiles;
  }

}
