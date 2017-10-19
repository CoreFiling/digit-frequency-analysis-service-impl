package com.corefiling.labs.digit.service.controllers;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.corefiling.labs.digit.model.StatusResponse;
import com.corefiling.labs.digit.model.StatusResponse.StatusEnum;

/**
 * Controller for the /status API endpoint.
 */
@Controller
@RequestMapping(value = "/status", produces = {APPLICATION_JSON_VALUE})
public class StatusController {
  @RequestMapping
  public ResponseEntity<StatusResponse> getStatus() {
    return ResponseEntity.<StatusResponse>ok(new StatusResponse()
      .name("CoreFiling Labs Digit Frequency Analysis API")
      .status(StatusEnum.OK)
      .apiVersion("0.1.0")
      .implVersion("0.1.0"));
  }
}
