package com.corefiling.labs.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.corefiling.labs.analysis.DigitAnalyser;
import com.corefiling.labs.analysis.FactRequester;
import com.corefiling.labs.analysis.impl.FactRequesterImpl;
<<<<<<< HEAD:src/main/java/com/corefiling/labs/service/DigitFrequencyAnalysisServiceImplConfiguration.java
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
=======
import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.model.AnalysisResponse;
import com.corefiling.nimbusTools.springBootBase.licensing.LicenceValidatorBuilder;
import com.corefiling.nimbusTools.springBootBase.licensing.ServiceInfo;
>>>>>>> develop:src/com/corefiling/labs/service/DigitFrequencyAnalysisServiceImplConfiguration.java

/**
 * Service configuration.
 */
@Configuration
public class DigitFrequencyAnalysisServiceImplConfiguration {

  @Value("${com.corefiling.labs.instanceServer}")
  private String _instanceServiceBasePath;

  @Bean
  @RequestScope
  public FactRequester factRequester(final HttpServletRequest request) {
    final String accessToken = request.getHeader("Authorization").replaceFirst("Bearer ", "");
    return new FactRequesterImpl(_instanceServiceBasePath, accessToken);
  }

  @Bean
<<<<<<< HEAD:src/main/java/com/corefiling/labs/service/DigitFrequencyAnalysisServiceImplConfiguration.java
  public ObjectMapper objectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(Include.NON_NULL);
    objectMapper.setSerializationInclusion(Include.NON_ABSENT);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setDateFormat(new ISO8601DateFormat());
    objectMapper.registerModule(new Jdk8Module());
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }
=======
  public DigitAnalyser digitAnalyser() {
    return facts -> new AnalysisResponse();
  }

>>>>>>> develop:src/com/corefiling/labs/service/DigitFrequencyAnalysisServiceImplConfiguration.java
}
