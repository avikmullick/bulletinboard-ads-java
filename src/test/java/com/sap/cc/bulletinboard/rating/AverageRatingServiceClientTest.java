package com.sap.cc.bulletinboard.rating;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sap.cc.bulletinboard.InvalidRequestException;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

public class AverageRatingServiceClientTest {
  public static final String WITH_VALID_ARGS = "perfect@example.com";
  public static final String WITH_UNKNOWN_ID = "abc";

  private AverageRatingServiceUrlProvider averageRatingServiceUrlProvider = Mockito.mock(AverageRatingServiceUrlProvider.class);
  private AverageRatingServiceClient averageRatingServiceClient;
  public static MockWebServer mockBackEnd;
  private ObjectMapper objectMapper = new ObjectMapper();

  @BeforeAll
  static void setUp() throws IOException {
    mockBackEnd = new MockWebServer();
    mockBackEnd.start();
  }

  @AfterAll
  static void tearDown() throws IOException {
    mockBackEnd.shutdown();
  }

  @BeforeEach
  void initialize() {
    String serviceUrl = String.format("http://localhost:%s/api/v1/averageRatings/{contact}", mockBackEnd.getPort());
    Mockito.when(averageRatingServiceUrlProvider.getServiceUrl()).thenReturn(serviceUrl);
    averageRatingServiceClient = new AverageRatingServiceClient(WebClient.create(), averageRatingServiceUrlProvider);
  }

  @Test
  public void whenCallingGetAverageRating_thenClientMakesCorrectCallToService() throws JsonProcessingException {
    mockBackEnd.enqueue(new MockResponse()
      .setBody(objectMapper.writeValueAsString(new AverageRatingResponse(3.25)))
      .addHeader(org.springframework.http.HttpHeaders.CONTENT_TYPE,
        org.springframework.http.MediaType.APPLICATION_JSON_VALUE));
    Assertions.assertEquals(averageRatingServiceClient.getAverageRating(WITH_VALID_ARGS),3.25);
  }

  @Test
  public void whenRequestingWithInvalidRequest_thenInvalidRequestExceptionIsThrown() {
    mockBackEnd.enqueue(new MockResponse().setResponseCode(400));
    org.assertj.core.api.Assertions.assertThatThrownBy(()-> averageRatingServiceClient.getAverageRating(WITH_UNKNOWN_ID)).isInstanceOf(
      InvalidRequestException.class);
  }
}