package com.corefiling.labs.digitFrequencyAnalysisServiceImpl.test;

import java.net.URI;

import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.corefiling.labs.digitFrequencyAnalysisService.ApiClient;

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

  protected ApiClient getClient() {
    final ApiClient client = new ApiClient();
    client.setBasePath(URI.create(HOST).resolve("/v1").toString());
    return client;
  }
}
