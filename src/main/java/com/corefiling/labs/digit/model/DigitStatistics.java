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

/** Represents the statistics for a single digit. */
public class DigitStatistics {

  private int _digit;
  private DigitProportion _proportion;

  public DigitStatistics setDigit(final int digit) {
    _digit = digit;
    return this;
  }

  public int getDigit() {
    return _digit;
  }

  public DigitStatistics setProportion(final DigitProportion proportion) {
    _proportion = proportion;
    return this;
  }

  public DigitProportion getProportion() {
    return _proportion;
  }

}
