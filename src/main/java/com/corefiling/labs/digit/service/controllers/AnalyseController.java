package com.corefiling.labs.digit.service.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corefiling.labs.digit.model.AnalysisResponse;
import com.corefiling.labs.digit.service.AnalyseApi;

/**
 * Controller.
 */
@Controller
public class AnalyseController {

  @Autowired
  private AnalyseApi _analyseApi;

  @RequestMapping(value = "/filing-versions/{filingVersionId}/digit-frequency", method = GET, produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<AnalysisResponse> getDigitFrequency(
      @PathVariable("filingVersionId") final UUID filingVersionId) throws Exception {
    return _analyseApi.getDigitFrequency(filingVersionId);
  }

}
