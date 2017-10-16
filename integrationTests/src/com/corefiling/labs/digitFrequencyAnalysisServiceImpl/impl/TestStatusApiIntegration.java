package com.corefiling.labs.digitFrequencyAnalysisServiceImpl.impl;

import static com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.ServiceConstants.API_VERSION_PREFIX;
import static com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.ServiceConstants.SERVICE_NAME;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.http.HttpStatus;

import com.corefiling.labs.digitFrequencyAnalysisService.ApiException;
import com.corefiling.labs.digitFrequencyAnalysisService.ApiResponse;
import com.corefiling.labs.digitFrequencyAnalysisService.StatusApi;
import com.corefiling.labs.digitFrequencyAnalysisService.model.StatusResponse;
import com.corefiling.labs.digitFrequencyAnalysisService.model.StatusResponse.StatusEnum;
import com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.AbstractApiClientIntegrationTest;

/**
 * Integration tests for the status API.
 */
public class TestStatusApiIntegration extends AbstractApiClientIntegrationTest {

  private static final Pattern VERSION_REGEX = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+(-dev\\.[\\d]+)?$");
  private static final Pattern EXTENDED_VERSION_REGEX = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+(\\-[a-zA-Z\\d\\-\\.]+)?(\\+[a-zA-Z\\d\\-\\.]+)?$");

  @Test
  public void getStatus() throws Exception {
    try {
      final Properties projectProperties = new Properties();
      projectProperties.load(new FileInputStream(new File("project.properties")));

      final ApiResponse<StatusResponse> response = new StatusApi(noRolesUser()).getStatusWithHttpInfo();
      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK.value());

      final StatusResponse status = response.getData();
      assertThat(status.getStatus()).isEqualTo(StatusEnum.OK);
      assertThat(status.getMessage()).isNull();
      assertThat(status.getName()).isEqualTo(SERVICE_NAME);
      assertWithMessage("Expected a valid semantic version").that(status.getApiVersion()).named("apiVersion").matches(VERSION_REGEX);
      assertThat(status.getApiVersion()).startsWith(API_VERSION_PREFIX);
      assertWithMessage("Expected a valid extended semantic version").that(status.getImplVersion()).named("implVersion").matches(EXTENDED_VERSION_REGEX);

      // If the following assert fails then you probably need to update the implementation
      // version number in DigitFrequencyAnalysisServiceImplConfiguration.serviceInfo() to agree
      // with the one in the project.properties file.
      final String projectVersion = projectProperties.get("version").toString();
      final String projectVersionBit = projectVersion.split("-")[0];
      assertThat(status.getImplVersion()).isEqualTo(projectVersionBit);
    }
    catch (final ApiException e) {
      throw new RuntimeException(String.format("ApiException: code=%s body=%s", e.getCode(), e.getResponseBody()), e);
    }
  }

}
