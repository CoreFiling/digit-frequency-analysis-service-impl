package com.corefiling.nimbus.componentScan;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.ApiComponentScanMarker;
import com.corefiling.labs.digitFrequencyAnalysisServiceImpl.DigitFrequencyAnalysisServiceImplComponentScanMarker;
import com.corefiling.shared.annotations.NoObfuscate;

/**
 * Stepping stone to get project component scanned.
 */
@NoObfuscate
@Configuration
@ComponentScan(basePackageClasses = {DigitFrequencyAnalysisServiceImplComponentScanMarker.class, ApiComponentScanMarker.class})
public class DigitFrequencyAnalysisServiceImplComponentScan {

}
