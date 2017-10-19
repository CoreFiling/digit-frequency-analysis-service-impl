package com.corefiling.labs.digit.analysis;

import java.util.List;

import com.corefiling.labs.digit.model.AnalysisResponse;

/** Analyses the distribution of the digits of facts. */
public interface DigitAnalyser {

  AnalysisResponse analyse(List<NumericFactValue> facts);

}
