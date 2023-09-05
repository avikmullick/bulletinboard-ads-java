package com.sap.cc.bulletinboard.rating;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cc.bulletinboard.InvalidRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.math.BigDecimal;

@Service
public class AverageRatingServiceClient {

  private Logger logger = LoggerFactory.getLogger(getClass());
  @Value("${service.averagerating.username}")
  private String averageratingServiceUsername = "";

  @Value("${service.averagerating.password}")
  private String averageratingServicePassword = "";

  @Autowired
  private ObjectMapper objectMapper;

  private final WebClient webClient;
  private final AverageRatingServiceUrlProvider averageratingServiceUrlProvider;

  public AverageRatingServiceClient(WebClient webClient, AverageRatingServiceUrlProvider averageratingServiceUrlProvider) {
    this.webClient = webClient;
    this.averageratingServiceUrlProvider = averageratingServiceUrlProvider;
  }

  public Double getAverageRating(String contact) {
    try {
      logger.info("ABCCCCC {} {}",averageratingServiceUrlProvider.getServiceUrl(),contact);
      AverageRatingResponse averageRatingResponse = webClient.get()
        .uri(averageratingServiceUrlProvider.getServiceUrl(), uri -> {
          logger.info("BVVV {}",uri.build(contact));
          return uri.build(contact);
        })
        .headers(httpHeaders -> httpHeaders.setBasicAuth(averageratingServiceUsername, averageratingServicePassword)).retrieve().bodyToMono(AverageRatingResponse.class).block();
      return averageRatingResponse.getAverageRating();
    }catch(WebClientResponseException.BadRequest exception){
      throw(new InvalidRequestException("Invalid Request",exception));
    }
  }
}
