package com.corefiling.labs.digit.model;

/** The expected value of the proportion at this percentile value. */
public class DigitPercentileProportionValue {

  private double _percentile;
  private double _value;

  public DigitPercentileProportionValue setPercentile(final double percentile) {
    _percentile = percentile;
    return this;
  }

  public double getPercentile() {
    return _percentile;
  }

  public DigitPercentileProportionValue setValue(final double value) {
    _value = value;
    return this;
  }

  public double getValue() {
    return _value;
  }

}
