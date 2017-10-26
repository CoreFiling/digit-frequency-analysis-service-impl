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
