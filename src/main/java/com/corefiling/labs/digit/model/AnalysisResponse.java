package com.corefiling.labs.digit.model;

import java.util.List;
import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * AnalysisResponse.
 */
public class AnalysisResponse {
  private Double _chiSquared = null;
  private Double _meanAbsoluteDeviation = null;
  private List<DigitStatistics> _digits = null;
  private Integer _analysedFactCount = null;

  public AnalysisResponse chiSquared(final Double chiSquared) {
    setChiSquared(chiSquared);
    return this;
  }

  /**
  * The number of facts analysed.
  * @return factsAnalysed
 **/
 public Integer getAnalysedFactCount() {
   return _analysedFactCount;
 }

 public void setAnalysedFactCount(final Integer analysedFactCount) {
   this._analysedFactCount = Preconditions.checkNotNull(analysedFactCount);
 }

 public AnalysisResponse analysedFactCount(final Integer analysedFactCount) {
   setAnalysedFactCount(analysedFactCount);
   return this;
 }

   /**
   * The chi^2 value.
   * @return chiSquared
  **/
  public Double getChiSquared() {
    return _chiSquared;
  }

  public void setChiSquared(final Double chiSquared) {
    this._chiSquared = Preconditions.checkNotNull(chiSquared);
  }

  public AnalysisResponse meanAbsoluteDeviation(final Double meanAbsoluteDeviation) {
    setMeanAbsoluteDeviation(meanAbsoluteDeviation);
    return this;
  }

   /**
   * The Mean Absolute Deviation (MAD) value.
   * @return meanAbsoluteDeviation
  **/
  public Double getMeanAbsoluteDeviation() {
    return _meanAbsoluteDeviation;
  }

  public void setMeanAbsoluteDeviation(final Double meanAbsoluteDeviation) {
    this._meanAbsoluteDeviation = Preconditions.checkNotNull(meanAbsoluteDeviation);
  }

  public AnalysisResponse digits(final List<DigitStatistics> digits) {
    setDigits(digits);
    return this;
  }

  public List<DigitStatistics> getDigits() {
    return _digits;
  }

  public void setDigits(final List<DigitStatistics> digits) {
    _digits = digits;
  }

  @Override
  public boolean equals(final java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final AnalysisResponse analysisResponse = (AnalysisResponse) o;
    return Objects.equals(this._chiSquared, analysisResponse._chiSquared)
        && Objects.equals(this._meanAbsoluteDeviation, analysisResponse._meanAbsoluteDeviation)
        && Objects.equals(this._analysedFactCount, analysisResponse._analysedFactCount)
        && Objects.equals(this._digits, analysisResponse._digits);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_chiSquared, _meanAbsoluteDeviation, _analysedFactCount, _digits);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class AnalysisResponse {\n");
    sb.append("    chiSquared: ").append(toIndentedString(_chiSquared)).append("\n");
    sb.append("    factsAnalysed: ").append(toIndentedString(_analysedFactCount)).append("\n");
    sb.append("    meanAbsoluteDeviation: ").append(toIndentedString(_meanAbsoluteDeviation)).append("\n");
    sb.append("    digits: ").append(toIndentedString(_digits)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(final java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

