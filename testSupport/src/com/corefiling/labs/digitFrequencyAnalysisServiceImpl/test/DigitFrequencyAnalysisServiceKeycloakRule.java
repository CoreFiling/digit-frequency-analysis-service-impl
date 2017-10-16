package com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test;

import java.util.function.Consumer;

import com.corefiling.nimbusTools.springBootBase.testSupport.keycloak.ApiClientKeycloakRule;
import com.corefiling.labs.digitFrequencyAnalysisService.ApiClient;

/**
 * Test rule for Digit Frequency Analysis Service.
 */
public class DigitFrequencyAnalysisServiceKeycloakRule extends ApiClientKeycloakRule<ApiClient> {

  // TODO: This has been pushed into spring-boot-base (nimbus-tools/spring-boot-base!220)
  ApiClient withUser(final ApiClient client, final String username, final String password) {
    return withUser(client, "integration-tests-login", username, password);
  }

  @Override
  protected ApiClient withClientSecret(final ApiClient apiClient, final String clientId, final String clientSecret) {
    return super.withClientSecret(apiClient, clientId, clientSecret);
  }

  @Override
  protected Consumer<String> setAccessToken(final ApiClient client) {
    return client::setAccessToken;
  }

  @Override
  protected void before() throws Throwable {
    // Stop spring-boot-base doing anything!
  }

  @Override
  protected String getKeycloakAuthUrl() {
    return System.getProperty("keycloak.authUrl", "http://localhost:7777/auth/");
  }
}
