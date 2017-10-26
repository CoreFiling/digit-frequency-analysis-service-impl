/*
 *  Copyright 2017 CoreFiling S.A.R.L.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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
