package com.sap.cc.bulletinboard.ads;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AdvertisementControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AdvertisementRepository storage;

  @BeforeEach
  public void beforeEach() {
    storage.deleteAll();
  }

  @Test
  public void getAll_noAds_returnsEmptyList() throws Exception {
    this.mockMvc.perform(get("/api/v1/ads")).andExpect(status().isOk()).andExpect(content().json("[]"));
  }

  @Test
  public void addAds_returnsCreatedAds() throws Exception {
    ResultActions resultActions=createAd(null,"Karnataka","SAP",BigDecimal.valueOf(200),"Rupee");
    MockHttpServletResponse response = resultActions
      .andExpect(status().isCreated())
      .andReturn().getResponse();
    //expects the response to contain a location header containing "/api/v1/ads/" + returnedAdId
    String responseHeader=response.getHeader("location");
    assertThat(responseHeader, containsString("/api/v1/ads/"));

  }

  public ResultActions createAd(Long id, String title,String contact, BigDecimal price,String currency) throws Exception {
    Advertisement advertisement=new Advertisement();
    advertisement.setId(id);
    advertisement.setTitle(title);
    advertisement.setContact(contact);
    advertisement.setPrice(price);
    advertisement.setCurrency(currency);
    // Methods for marshalling and unmarshalling objects to/from json
    String jsonBody = objectMapper.writeValueAsString(advertisement);
    Advertisement advertisementObject = objectMapper.readValue(jsonBody, Advertisement.class);
    ResultActions resultActions=this.mockMvc.perform(post("/api/v1/ads").content(jsonBody)
      .contentType(org.springframework.http.MediaType.APPLICATION_JSON));
    return resultActions;
  }

  @Test
  public void addAdAndGetSingle_returnsAd() throws Exception {
    ResultActions resultActions=createAd(1l,"Mumbai","SAP",BigDecimal.valueOf(200),"Rupee");
    MockHttpServletResponse response = resultActions
      .andExpect(status().isCreated())
      .andReturn().getResponse();
    //expects the response to contain a location header containing "/api/v1/ads/" + returnedAdId
    String responseHeader=response.getHeader("location");
    assertThat(responseHeader, containsString("/api/v1/ads/"));

    this.mockMvc.perform(get(response.getHeader("location"))).andExpect(status().isOk());

  }

  @Test
  public void getSingle_noAds_returnsNotFound() throws Exception {
    this.mockMvc.perform(get("/api/v1/ads/2")).andExpect(status().isNotFound());
  }

  @Test
  public void addMultipleAndGetAll_returnsAddedAds() throws Exception {
    ResultActions resultActions=createAd(1l,"Mumbai","SAP",BigDecimal.valueOf(200),"Rupee");
    MockHttpServletResponse response = resultActions
      .andExpect(status().isCreated())
      .andReturn().getResponse();
    //expects the response to contain a location header containing "/api/v1/ads/" + returnedAdId
    String responseHeader=response.getHeader("location");
    assertThat(responseHeader, containsString("/api/v1/ads/"));

    ResultActions resultActionsGetAll=this.mockMvc.perform(get("/api/v1/ads"));
    resultActionsGetAll.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(1)));


    resultActions=createAd(1l,"WestBengal","SAP",BigDecimal.valueOf(200),"Rupee");
    response = resultActions
      .andExpect(status().isCreated())
      .andReturn().getResponse();
    //expects the response to contain a location header containing "/api/v1/ads/" + returnedAdId
    responseHeader=response.getHeader("location");
    assertThat(responseHeader, containsString("/api/v1/ads/"));

    resultActionsGetAll=this.mockMvc.perform(get("/api/v1/ads"));
    resultActionsGetAll.andExpect(status().isOk()).andExpect(jsonPath("$.length()", is(2)));

  }

  @Test
  public void getSingle_idLessThanOne_returnsBadRequest() throws Exception {
    this.mockMvc.perform(get("/api/v1/ads/0")).andExpect(status().isBadRequest());
  }

}