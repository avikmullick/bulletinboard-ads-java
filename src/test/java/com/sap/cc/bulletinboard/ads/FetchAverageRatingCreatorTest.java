package com.sap.cc.bulletinboard.ads;

import com.sap.cc.bulletinboard.rating.AverageRatingServiceClient;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class FetchAverageRatingCreatorTest {
  @Mock
  private AverageRatingServiceClient averageRatingServiceClient;

  @InjectMocks
  private FetchAverageRatingCreator fetchAverageRatingCreator;

  @Test
  void shouldCreateAPrettyUserPage() {
    Advertisement advertisement = new Advertisement("someName");
    Mockito.when(averageRatingServiceClient.getAverageRating(ArgumentMatchers.any())).thenReturn(3.5);
    assertThat(fetchAverageRatingCreator.getAverageRating(advertisement)).isEqualTo(3.5);
  }
}