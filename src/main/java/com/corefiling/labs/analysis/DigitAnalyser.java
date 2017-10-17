package com.corefiling.labs.analysis;

import java.util.List;

import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.model.AnalysisResponse;

/** Analyses the distribution of the digits of facts. */
public interface DigitAnalyser {

  AnalysisResponse analyse(List<NumericFactValue> facts);

}
