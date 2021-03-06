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

import static com.corefiling.labs.digit.test.ServiceConstants.API_VERSION_PREFIX;
import static com.corefiling.labs.digit.test.ServiceConstants.SERVICE_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.corefiling.labs.digit.test.AbstractApiClientIntegrationTest;
import com.corefiling.labs.digitFrequencyAnalysisService.ApiException;
import com.corefiling.labs.digitFrequencyAnalysisService.ApiResponse;
import com.corefiling.labs.digitFrequencyAnalysisService.StatusApi;
import com.corefiling.labs.digitFrequencyAnalysisService.model.StatusResponse;
import com.corefiling.labs.digitFrequencyAnalysisService.model.StatusResponse.StatusEnum;

/**
 * Integration tests for the status API.
 */
public class TestStatusControllerIntegration extends AbstractApiClientIntegrationTest {

  private static final Pattern VERSION_REGEX = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+(-dev\\.[\\d]+)?$");
  private static final Pattern EXTENDED_VERSION_REGEX = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+(\\-[a-zA-Z\\d\\-\\.]+)?(\\+[a-zA-Z\\d\\-\\.]+)?$");

  @Test
  public void getStatus() throws Exception {
    try {
      final Properties gradleProperties = new Properties();
      gradleProperties.load(new FileInputStream(new File("gradle.properties")));

      final ApiResponse<StatusResponse> response = new StatusApi(createAuthenticatedClient()).getStatusWithHttpInfo();
      assertThat(response.getStatusCode(), equalTo(HttpStatus.OK.value()));

      final StatusResponse status = response.getData();
      assertThat(status.getStatus(), equalTo(StatusEnum.OK));
      assertThat(status.getMessage(), nullValue());
      assertThat(status.getName(), equalTo(SERVICE_NAME));
      assertThat("Expected a valid semantic version", status.getApiVersion(), matchesPattern(VERSION_REGEX));
      assertThat(status.getApiVersion(), startsWith(API_VERSION_PREFIX));
      assertThat("Expected a valid extended semantic version", status.getImplVersion(), matchesPattern(EXTENDED_VERSION_REGEX));

      // If the following assert fails then you probably need to update the implementation
      // version number in DigitFrequencyAnalysisServiceImplConfiguration.serviceInfo() to agree
      // with the one in the gradle.properties file.
      final String projectVersion = gradleProperties.get("version").toString();
      final String projectVersionBit = projectVersion.split("-")[0];
      assertThat(status.getImplVersion(), equalTo(projectVersionBit));
    }
    catch (final ApiException e) {
      throw new RuntimeException(String.format("ApiException: code=%s body=%s", e.getCode(), e.getResponseBody()), e);
    }
  }

}
