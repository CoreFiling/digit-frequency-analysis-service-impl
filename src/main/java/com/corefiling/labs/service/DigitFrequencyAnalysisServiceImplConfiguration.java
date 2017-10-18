package com.corefiling.labs.service;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import com.corefiling.labs.analysis.DigitAnalyser;
import com.corefiling.labs.analysis.FactRequester;
import com.corefiling.labs.analysis.impl.DigitAnalyserImpl;
import com.corefiling.labs.analysis.impl.FactRequesterImpl;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;


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
    final String accessToken = Optional.ofNullable(request.getHeader("Authorization")).map(s -> s.replaceFirst("Bearer ", "")).orElse(null);
    return new FactRequesterImpl(_instanceServiceBasePath, accessToken);
  }

  @Bean
  public DigitAnalyser digitAnalyser() {
    return new DigitAnalyserImpl();
  }

  @Bean
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

}
