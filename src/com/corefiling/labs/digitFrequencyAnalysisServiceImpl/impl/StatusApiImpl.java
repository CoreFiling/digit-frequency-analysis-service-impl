package com.corefiling.labs.digitFrequencyAnalysisServiceImpl.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.corefiling.nimbusTools.springBootBase.licensing.ApiInfo;
import com.corefiling.nimbusTools.springBootBase.licensing.ServiceInfo;
import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.StatusApi;
import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.model.StatusResponse;
import com.corefiling.labs.digitFrequencyAnalysisService.abstractSpringBoot.model.StatusResponse.StatusEnum;

/**
 * Implementation of the status API.
 */
@Service
public class StatusApiImpl implements StatusApi {

  @Autowired
  private ApiInfo _apiInfo;

  @Autowired
  private ServiceInfo _serviceInfo;

  @Override
  public ResponseEntity<StatusResponse> getStatus() {
    final StatusResponse response = new StatusResponse();
    response.setName(_apiInfo.getTitle());
    response.setStatus(StatusEnum.OK);
    response.setApiVersion(_apiInfo.getVersion());
    response.setImplVersion(_serviceInfo.getVersion());

    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
