package com.corefiling.labs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import com.corefiling.labs.service.ComponentScanMarker;

/**
 * Spring boot application entry point.
 */
@SpringBootApplication
@ComponentScan(basePackageClasses = {ComponentScanMarker.class})
public class Application {
  public static void main(final String[] args) {
    SpringApplication.run(Application.class, args);
  }
}
