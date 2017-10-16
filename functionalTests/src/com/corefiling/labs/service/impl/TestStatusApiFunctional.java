package com.corefiling.labs.service.impl;

import static com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.ServiceConstants.API_VERSION_PREFIX;
import static com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.ServiceConstants.SERVICE_NAME;
import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.model.StatusResponse;
import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.model.StatusResponse.StatusEnum;
import com.corefiling.labs.service.DigitFrequencyAnalysisServiceImplConfiguration;
import com.corefiling.nimbusTools.springBootBase.testSupport.AbstractSpringBootFunctionalTest;

/**
 * Functional tests for {@link StatusApiImpl}.
 */
public class TestStatusApiFunctional extends AbstractSpringBootFunctionalTest {

  private static final Pattern VERSION_REGEX = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+(-dev\\.[\\d]+)?$");
  private static final Pattern EXTENDED_VERSION_REGEX = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+(\\-[a-zA-Z\\d\\-\\.]+)?(\\+[a-zA-Z\\d\\-\\.]+)?$");

  @Autowired
  private StatusApiImpl _sut;

  @Test
  public void statusIsOK() throws Exception {
    final StatusResponse statusResponse = _sut.getStatus().getBody();
    assertThat(statusResponse.getStatus()).isEqualTo(StatusEnum.OK);
    assertThat(statusResponse.getMessage().isPresent()).isFalse();
  }

  @Test
  public void nameIsServiceName() throws Exception {
    final StatusResponse statusResponse = _sut.getStatus().getBody();
    assertThat(statusResponse.getName()).isEqualTo(SERVICE_NAME);
  }

  @Test
  public void apiVersionPrefixMatchesIvyXml() throws Exception {
    final StatusResponse statusResponse = _sut.getStatus().getBody();
    assertWithMessage("Expected a valid semantic version").that(statusResponse.getApiVersion()).named("apiVersion").matches(VERSION_REGEX);
    assertThat(statusResponse.getApiVersion()).startsWith(API_VERSION_PREFIX);
  }

  /**
   * Check version in
   * {@link DigitFrequencyAnalysisServiceImplConfiguration#serviceInfo()} matches
   * project.properties.
   */
  @Test
  public void implVersionMatchesProjectProperties() throws Exception {
    final StatusResponse statusResponse = _sut.getStatus().getBody();
    assertWithMessage("Expected a valid extended semantic version").that(statusResponse.getImplVersion()).named("implVersion").matches(EXTENDED_VERSION_REGEX);

    // If the following assert fails then you probably need to update the
    // implementation version number in
    // DigitFrequencyAnalysisServiceImplConfiguration.serviceInfo() to agree with the one in
    // the project.properties file.
    final Properties projectProperties = new Properties();
    projectProperties.load(new FileInputStream(new File("project.properties")));
    final String projectVersion = projectProperties.get("version").toString();
    final String projectVersionBit = projectVersion.split("-")[0];
    assertThat(statusResponse.getImplVersion()).isEqualTo(projectVersionBit);
  }
}
