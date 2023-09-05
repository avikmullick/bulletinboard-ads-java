package com.sap.cc.bulletinboard.rating;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AverageRatingServiceUrlProvider {
  @Value("${service.averagerating.url}")
  private String serviceUrl;

  public String getServiceUrl() {
    return serviceUrl;
  }
}
