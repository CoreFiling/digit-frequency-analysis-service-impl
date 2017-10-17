package com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test;

import java.net.URI;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * Base class for tests against the Digit Frequency Analysis Service API client.
 */
public class AbstractApiClientIntegrationTest {

  private static final String HOST = System.getProperty("service.url", "http://localhost:8601");

  private final ExpectedException _thrown = ExpectedException.none();

  @Rule
  public ExpectedException thrown() {
    return _thrown;
  }

  protected com.corefiling.labs.digitFrequencyAnalysisService.ApiClient getClient() {
    final com.corefiling.labs.digitFrequencyAnalysisService.ApiClient client = new com.corefiling.labs.digitFrequencyAnalysisService.ApiClient();
    client.setBasePath(URI.create(HOST).resolve("/v1").toString());
    return client;
  }

  protected com.corefiling.platform.instanceService.ApiClient instanceServiceClient() {
    final com.corefiling.platform.instanceService.ApiClient client = new com.corefiling.platform.instanceService.ApiClient();
    client.setBasePath(URI.create(HOST).resolve("/v1").toString());
    return client;
  }
}
