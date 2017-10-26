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

package com.corefiling.labs.digit.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.corefiling.labs.digit.analysis.DigitAnalyser;
import com.corefiling.labs.digit.analysis.FactRequester;
import com.corefiling.labs.digit.analysis.NumericFactValue;
import com.corefiling.labs.digit.model.AnalysisResponse;
import com.corefiling.labs.digit.service.AnalyseApi;

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
