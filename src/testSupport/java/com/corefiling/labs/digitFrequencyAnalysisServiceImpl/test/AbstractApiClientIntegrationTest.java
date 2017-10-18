package com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test;

import static java.util.Collections.singletonMap;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.oltu.oauth2.client.OAuthClient;
import org.apache.oltu.oauth2.client.URLConnectionClient;
import org.apache.oltu.oauth2.client.request.OAuthClientRequest;
import org.apache.oltu.oauth2.client.response.OAuthJSONAccessTokenResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.apache.oltu.oauth2.common.exception.OAuthSystemException;
import org.apache.oltu.oauth2.common.message.types.GrantType;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;

/**
 * Base class for tests against the Digit Frequency Analysis Service API client.
 */
public class AbstractApiClientIntegrationTest {

  private static final String HOST = System.getProperty("service.url", "http://localhost:8601/");
  private static final String INSTANCE_SERVICE_HOST = System.getProperty("com.corefiling.labs.instanceServer", "https://platform-api.cfl.io/instance-service/");
  private static final String KEYCLOAK_HOST = System.getProperty("keycloak.url", "https://login.corefiling.com/auth");

  private final ExpectedException _thrown = ExpectedException.none();

  @Rule
  public ExpectedException thrown() {
    return _thrown;
  }

  protected com.corefiling.labs.digitFrequencyAnalysisService.ApiClient createAuthenticatedClient() {
    final com.corefiling.labs.digitFrequencyAnalysisService.ApiClient client = new com.corefiling.labs.digitFrequencyAnalysisService.ApiClient();
    client.setBasePath(URI.create(HOST).resolve("v1/").toString());
    client.setAccessToken(getToken());
    client.getHttpClient().setReadTimeout(10, TimeUnit.SECONDS);
    return client;
  }

  protected com.corefiling.platform.instanceService.ApiClient createAuthenticatedInstanceServiceClient() {
    final com.corefiling.platform.instanceService.ApiClient client = new com.corefiling.platform.instanceService.ApiClient();
    client.setBasePath(URI.create(INSTANCE_SERVICE_HOST).resolve("v1/").toString());
    client.setAccessToken(getToken());
    return client;
  }

  protected AuthzClient getAuthz() {
    final HttpClient client = HttpClientBuilder.create().build();
    return AuthzClient.create(new Configuration(KEYCLOAK_HOST, "dev", "document-service", singletonMap("secret", System.getenv("DOCUMENT_SERVICE_SECRET")), client));
  }

  private String getToken() throws AssertionError {
    try {
      final OAuthClientRequest request =
          OAuthClientRequest.tokenLocation(
              new URI(KEYCLOAK_HOST + "/")
              .resolve("realms/dev/protocol/openid-connect/token")
              .toString()
              )
          .setGrantType(GrantType.PASSWORD)
          .setClientId("labs-integration-tests")
          .setClientSecret(System.getenv("LABS_INTEGRATION_TESTS_SECRET"))
          .setUsername("labs-integration-test-user")
          .setPassword(System.getenv("LABS_INTEGRATION_TESTS_PASSWORD"))
          .buildBodyMessage();
      final OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
      final OAuthJSONAccessTokenResponse tokenResponse = oAuthClient.accessToken(request);
      return tokenResponse.getAccessToken();
    }
    catch (OAuthSystemException | OAuthProblemException | URISyntaxException e) {
      throw new AssertionError(e);
    }
  }

}
