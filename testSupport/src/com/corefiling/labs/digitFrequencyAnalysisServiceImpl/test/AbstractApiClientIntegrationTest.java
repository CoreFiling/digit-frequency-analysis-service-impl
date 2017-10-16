package com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.util.JsonSerialization;
import org.springframework.http.HttpStatus;

import com.corefiling.nimbusTools.springBootBase.protectedResources.KeycloakProtectedResources;
import com.corefiling.nimbusTools.springBootBase.testSupport.AbstractIntegrationTest;
import com.corefiling.labs.digitFrequencyAnalysisService.ApiClient;
import com.corefiling.labs.digitFrequencyAnalysisService.ApiException;
import com.fasterxml.jackson.databind.DeserializationFeature;

/**
 * Base class for tests against the Digit Frequency Analysis Service API client.
 */
public class AbstractApiClientIntegrationTest extends AbstractIntegrationTest {

  private final ExpectedException _thrown = ExpectedException.none();

  private final DigitFrequencyAnalysisServiceKeycloakRule _rule = new DigitFrequencyAnalysisServiceKeycloakRule();

  @Rule
  public ExpectedException thrown() {
    return _thrown;
  }

  @Rule
  public DigitFrequencyAnalysisServiceKeycloakRule keycloak() {
    return _rule;
  }

  private ApiClient getClient() {
    final ApiClient client = new ApiClient();
    client.setBasePath(getURI("/v1").toString());
    return client;
  }

  protected ApiClient noRolesUser() {
    return keycloak().withUser(getClient(), "no-roles", "password");
  }

  protected ApiClient internalClient() {
    return keycloak().withClientSecret(getClient(), "integration-tests", "integration-tests-secret");
  }

  protected KeycloakProtectedResources getProtectedResources() {
    return new KeycloakProtectedResources(getAuthzClient());
  }

  protected AuthzClient getAuthzClient() {
    final boolean failOnUnknownProperties = isFailOnUnknownProperties();
    setFailOnUnknownProperties(false);
    try {
      return AuthzClient.create();
    }
    finally {
      setFailOnUnknownProperties(failOnUnknownProperties);
    }
  }

  private static void setFailOnUnknownProperties(final boolean failOnUnknownProperties) {
    JsonSerialization.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, failOnUnknownProperties);
  }

  private static boolean isFailOnUnknownProperties() {
    final int deserializationFeatures = JsonSerialization.mapper.getDeserializationConfig().getDeserializationFeatures();
    return (deserializationFeatures & DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES.getMask()) != 0;
  }
}
