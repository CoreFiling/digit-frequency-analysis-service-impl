package com.corefiling.labs.digit.model;

import java.util.Objects;

import com.google.common.base.Preconditions;

/**
 * AnalysisResponse.
 */
public class AnalysisResponse {
  private Double _chiSquared = null;
  private Double _meanAbsoluteDeviation = null;
  private Integer _factsAnalysed = null;

  public AnalysisResponse chiSquared(final Double chiSquared) {
    setChiSquared(chiSquared);
    return this;
  }

  /**
  * The number of facts analysed.
  * @return factsAnalysed
 **/
 public Integer getFactsAnalysed() {
   return _factsAnalysed;
 }

 public void setFactsAnalysed(final Integer factsAnalysed) {
   this._factsAnalysed = Preconditions.checkNotNull(factsAnalysed);
 }

 public AnalysisResponse factsAnalysed(final Integer factsAnalysed) {
   setFactsAnalysed(factsAnalysed);
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
        && Objects.equals(this._factsAnalysed, analysisResponse._factsAnalysed)
        && Objects.equals(this._meanAbsoluteDeviation, analysisResponse._meanAbsoluteDeviation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(_chiSquared, _factsAnalysed, _meanAbsoluteDeviation);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class AnalysisResponse {\n");
    sb.append("    chiSquared: ").append(toIndentedString(_chiSquared)).append("\n");
    sb.append("    factsAnalysed: ").append(toIndentedString(_factsAnalysed)).append("\n");
    sb.append("    meanAbsoluteDeviation: ").append(toIndentedString(_meanAbsoluteDeviation)).append("\n");
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

