package com.sap.cc.bulletinboard.rating;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class AverageRatingResponse {
  @JsonProperty("average_rating")
  private Double averageRating;

  public AverageRatingResponse(){

  }
  public AverageRatingResponse(Double averageRating) {
    this.averageRating=averageRating;
  }
  public Double getAverageRating() {
    return averageRating;
  }
}
