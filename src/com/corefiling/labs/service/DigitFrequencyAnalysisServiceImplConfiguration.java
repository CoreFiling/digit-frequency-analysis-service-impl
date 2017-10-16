package com.corefiling.labs.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corefiling.nimbusTools.springBootBase.licensing.LicenceValidatorBuilder;
import com.corefiling.nimbusTools.springBootBase.licensing.ServiceInfo;

/**
 * Service configuration.
 */
@Configuration
public class DigitFrequencyAnalysisServiceImplConfiguration {
  @Bean
  public ServiceInfo serviceInfo() {
    return () -> "0.1.0";
  }

  /**
   * Delete this bean to enable licence checks.
   */
  @Bean
  public LicenceValidatorBuilder getLicenceValidatorBuilder() {
    return LicenceValidatorBuilder.NO_LICENSING;
  }
}
