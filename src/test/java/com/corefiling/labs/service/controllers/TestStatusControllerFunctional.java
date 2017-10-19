package com.corefiling.labs.service.controllers;

import static com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.ServiceConstants.API_VERSION_PREFIX;
import static com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test.ServiceConstants.SERVICE_NAME;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.regex.Pattern;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import com.corefiling.labs.digit.Application;
import com.corefiling.labs.digit.model.StatusResponse;
import com.corefiling.labs.digit.model.StatusResponse.StatusEnum;
import com.corefiling.labs.digit.service.DigitFrequencyAnalysisServiceImplConfiguration;
import com.corefiling.labs.digit.service.controllers.StatusController;

/**
 * Functional tests for {@link StatusController}.
 */
@SpringBootTest(classes = {Application.class}, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TestStatusControllerFunctional {

  private static final Pattern VERSION_REGEX = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+(-dev\\.[\\d]+)?$");
  private static final Pattern EXTENDED_VERSION_REGEX = Pattern.compile("^[\\d]+\\.[\\d]+\\.[\\d]+(\\-[a-zA-Z\\d\\-\\.]+)?(\\+[a-zA-Z\\d\\-\\.]+)?$");

  @ClassRule
  public static final SpringClassRule SCR = new SpringClassRule();

  @Rule
  public final SpringMethodRule _springMethodRule = new SpringMethodRule();

  @Autowired
  private StatusController _sut;

  @Test
  public void statusIsOK() throws Exception {
    final StatusResponse statusResponse = _sut.getStatus().getBody();
    assertThat(statusResponse.getStatus(), equalTo(StatusEnum.OK));
    assertThat(statusResponse.getMessage().isPresent(), is(false));
  }

  @Test
  public void nameIsServiceName() throws Exception {
    final StatusResponse statusResponse = _sut.getStatus().getBody();
    assertThat(statusResponse.getName(), equalTo(SERVICE_NAME));
  }

  @Test
  public void apiVersionPrefixMatchesIvyXml() throws Exception {
    final StatusResponse statusResponse = _sut.getStatus().getBody();
    assertThat("Expected a valid semantic version", statusResponse.getApiVersion(), matchesPattern(VERSION_REGEX));
    assertThat(statusResponse.getApiVersion(), startsWith(API_VERSION_PREFIX));
  }

  /**
   * Check version in
   * {@link DigitFrequencyAnalysisServiceImplConfiguration#serviceInfo()} matches
   * gradle.properties.
   */
  @Test
  public void implVersionMatchesProjectProperties() throws Exception {
    final StatusResponse statusResponse = _sut.getStatus().getBody();
    assertThat("Expected a valid extended semantic version", statusResponse.getImplVersion(), matchesPattern(EXTENDED_VERSION_REGEX));

    // If the following assert fails then you probably need to update the
    // implementation version number in
    // DigitFrequencyAnalysisServiceImplConfiguration.serviceInfo() to agree with the one in
    // the gradle.properties file.
    final Properties projectProperties = new Properties();
    projectProperties.load(new FileInputStream(new File("gradle.properties")));
    final String projectVersion = projectProperties.get("version").toString();
    final String projectVersionBit = projectVersion.split("-")[0];
    assertThat(statusResponse.getImplVersion(), equalTo(projectVersionBit));
  }
}
