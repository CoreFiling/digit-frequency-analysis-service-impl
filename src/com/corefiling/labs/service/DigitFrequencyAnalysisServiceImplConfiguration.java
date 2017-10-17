package com.corefiling.labs.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.corefiling.labs.analysis.DigitAnalyser;
import com.corefiling.labs.analysis.FactRequester;
import com.corefiling.labs.analysis.impl.FactRequesterImpl;
import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.model.AnalysisResponse;
import com.corefiling.nimbusTools.springBootBase.licensing.LicenceValidatorBuilder;
import com.corefiling.nimbusTools.springBootBase.licensing.ServiceInfo;

/**
 * Service configuration.
 */
@Configuration
public class DigitFrequencyAnalysisServiceImplConfiguration {

  @Value("${com.corefiling.labs.instanceServer}")
  private String _instanceServiceBasePath;

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

  @Bean
  @RequestScope
  public FactRequester factRequester(final HttpServletRequest request) {
    final String accessToken = request.getHeader("Authorization").replaceFirst("Bearer ", "");
    return new FactRequesterImpl(_instanceServiceBasePath, accessToken);
  }

  @Bean
  public DigitAnalyser digitAnalyser() {
    return facts -> new AnalysisResponse();
  }

}
