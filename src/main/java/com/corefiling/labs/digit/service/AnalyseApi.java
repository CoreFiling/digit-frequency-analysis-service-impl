package com.corefiling.labs.digit.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.corefiling.labs.digit.model.AnalysisResponse;

/**
 * Analyse API.
 */
public interface AnalyseApi {

  ResponseEntity<AnalysisResponse> analyseFiling(UUID filingVersionId) throws Exception;

}
