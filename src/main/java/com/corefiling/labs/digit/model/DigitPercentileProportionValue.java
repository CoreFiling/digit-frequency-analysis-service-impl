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
