package com.corefiling.labs.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.corefiling.labs.analysis.DigitAnalyser;
import com.corefiling.labs.analysis.FactRequester;
import com.corefiling.labs.analysis.NumericFactValue;
import com.corefiling.labs.model.AnalysisResponse;
import com.corefiling.labs.service.AnalyseApi;

/**
 * Implementation of the analysis API.
 */
@Service
public class AnalyseApiImpl implements AnalyseApi {

  @Autowired
  private FactRequester _factRequester;

  @Autowired
  private DigitAnalyser _digitAnalyser;

  @Override
  public ResponseEntity<AnalysisResponse> analyseFiling(final UUID filingVersionId) throws Exception {
    final List<NumericFactValue> facts = _factRequester.getFacts(filingVersionId);
    final AnalysisResponse response = _digitAnalyser.analyse(facts);
    return ResponseEntity.ok(response);
  }

}
