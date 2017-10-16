package com.corefiling.labs.service.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corefiling.labs.model.AnalysisResponse;

/**
 * Controller for the /analyse API endpoint.
 */
@Controller
@RequestMapping(value = "/analyse", produces = {APPLICATION_JSON_VALUE})
public class AnalyseController {

  @RequestMapping("/{filingVersionId}")
  public ResponseEntity<AnalysisResponse> analyseFiling(
      @PathVariable("filingVersionId") final UUID filingVersionId) throws Exception {
    throw new UnsupportedOperationException("Not implemented");
  }

}
