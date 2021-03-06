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

package com.corefiling.labs.digit.test;

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
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClient;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;

/**
 * Base class for tests against the Digit Frequency Analysis Service API client.
 */
public class AbstractApiClientIntegrationTest {

  private static final String HOST = System.getProperty("service.url", "http://localhost:8601/");
  private static final String INSTANCE_SERVICE_HOST = System.getProperty("com.corefiling.labs.digit.instanceServer", "https://platform-api.cfl.io/instance-service/");
  private static final String KEYCLOAK_HOST = System.getProperty("keycloak.url", "https://login.corefiling.com/auth");

  private final ExpectedException _thrown = ExpectedException.none();

  @Rule
  public ExpectedException thrown() {
    return _thrown;
  }

  protected com.corefiling.labs.digitFrequencyAnalysisService.ApiClient createAuthenticatedClient() {
    final com.corefiling.labs.digitFrequencyAnalysisService.ApiClient client = new com.corefiling.labs.digitFrequencyAnalysisService.ApiClient();
    client.setBasePath(URI.create(HOST).resolve("v1").toString());
    client.setAccessToken(getToken());
    client.getHttpClient().setReadTimeout(1, TimeUnit.MINUTES);
    return client;
  }

  protected com.corefiling.platform.instanceService.ApiClient createAuthenticatedInstanceServiceClient() {
    final com.corefiling.platform.instanceService.ApiClient client = new com.corefiling.platform.instanceService.ApiClient();
    final JerseyClient http = (JerseyClient) client.getHttpClient();
    final Integer oneMinute = 60000;
    http.property(ClientProperties.READ_TIMEOUT, oneMinute);
    client.setBasePath(URI.create(INSTANCE_SERVICE_HOST).resolve("v1").toString());
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
