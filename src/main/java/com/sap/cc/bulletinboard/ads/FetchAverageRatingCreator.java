package com.sap.cc.bulletinboard.ads;

import com.sap.cc.bulletinboard.rating.AverageRatingServiceClient;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FetchAverageRatingCreator {
  private AverageRatingServiceClient averageRatingServiceClient;
  public FetchAverageRatingCreator(AverageRatingServiceClient averageRatingServiceClient) {
    this.averageRatingServiceClient=averageRatingServiceClient;
  }

  public Double getAverageRating(Advertisement advertisement) {
    Double averageRatingResponse=averageRatingServiceClient.getAverageRating(advertisement.getContact());
    if(averageRatingResponse!=null){
      return averageRatingResponse;
    } else {
      return 0.0;
    }
  }
}
