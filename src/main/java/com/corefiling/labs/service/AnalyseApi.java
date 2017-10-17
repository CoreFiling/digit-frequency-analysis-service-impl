package com.corefiling.labs.service;

import java.util.UUID;

import org.springframework.http.ResponseEntity;

import com.corefiling.labs.model.AnalysisResponse;

/**
 * Analyse API.
 */
public interface AnalyseApi {

  ResponseEntity<AnalysisResponse> analyseFiling(UUID filingVersionId) throws Exception;

}
