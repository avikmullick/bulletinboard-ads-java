package com.sap.cc.bulletinboard.ads;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
public class AdvertisementRepositoryTest {
  @Autowired
  private AdvertisementRepository repository;

  @Autowired
  private AdvertisementRepository adStorage;
  @BeforeEach
  public void clearDb(){
    repository.deleteAll();
    adStorage.deleteAll();
  }

  @Test
  public void findAll(){
    List<Advertisement> advertisementList = repository.findAll();
    Assertions.assertThat(advertisementList).isEmpty();
  }

  @Test
  public void adMultipleAction(){
    Advertisement advertisement=new Advertisement();
    advertisement.setTitle("Karnataka");
    advertisement.setContact("SAP");
    advertisement.setPrice(BigDecimal.ONE);
    Advertisement createdAd= repository.save(advertisement);
    List<Advertisement> advertisementList = repository.findAll();
    Assertions.assertThat(advertisementList).hasSize(1);
    Assertions.assertThat(createdAd.getTitle()).isEqualTo(advertisement.getTitle());
    Assertions.assertThat(createdAd.getContact()).isEqualTo(advertisement.getContact());
    Assertions.assertThat(createdAd.getContact()).isEqualTo(advertisement.getContact());
    Assertions.assertThat(createdAd.getPrice()).isEqualTo(advertisement.getPrice());
  }

  @Test
  public void findAdByTitle(){
    Advertisement advertisement=new Advertisement();
    advertisement.setTitle("Karnataka");
    advertisement.setContact("SAP");
    advertisement.setPrice(BigDecimal.ONE);
    Advertisement sapAd=repository.save(advertisement);
    Assertions.assertThat(sapAd.getTitle()).isEqualTo(advertisement.getTitle());
  }



  private final Advertisement AD_BIKE = createAdvertisementWithTitleContactPriceCurrency(
    "Bike for Sale", "Bike-sellers", BigDecimal.ONE, "USD");
  private final Advertisement AD_PAN = createAdvertisementWithTitleContactPriceCurrency(
    "Lightly used pan", "Second-hand Pans", BigDecimal.TEN, "EUR");

  @Test
  public void testRetrieveAdvertisementByIdNonExistingAdvertisement() {
    Optional<Advertisement> returnedAdvertisement = adStorage.findById(1L);
    assertThat(returnedAdvertisement.isPresent(), is(false));
  }

  @Test
  public void testSaveAdvertisement() {
    Advertisement returnedAdvertisement = adStorage.save(AD_BIKE);

    assertThat(returnedAdvertisement.getTitle(), is(AD_BIKE.getTitle()));
    assertThat(returnedAdvertisement.getContact(), is(AD_BIKE.getContact()));
    assertThat(returnedAdvertisement.getPrice(), is(BigDecimal.ONE));
    assertThat(returnedAdvertisement.getCurrency(), is("USD"));
  }

  @Test
  public void testSaveTwoAdvertisements() {
    adStorage.save(AD_BIKE);

    Advertisement returnedAdvertisement = adStorage.save(AD_PAN);

    assertThat(returnedAdvertisement.getTitle(), is(AD_PAN.getTitle()));
    assertThat(returnedAdvertisement.getContact(), is(AD_PAN.getContact()));
    assertThat(returnedAdvertisement.getPrice(), is(BigDecimal.TEN));
    assertThat(returnedAdvertisement.getCurrency(), is("EUR"));
  }

  @Test
  public void testSaveAdvertisementTryToForceId() {
    Advertisement ad = AD_BIKE;
    ad.setId(200L);
    Advertisement returnedAdvertisement = adStorage.save(ad);

    assertThat(returnedAdvertisement.getId(),not(200L));
  }

/*  @Test
  public void testSaveAndRetrieveAdvertisementById() {
    adStorage.save(AD_BIKE);

    Optional<Advertisement> returnedAdvertisement = adStorage.findById(1L);

    assertThat(returnedAdvertisement.isPresent(), is(true));
    assertThat(returnedAdvertisement.get().getId(), is(1L));
    assertThat(returnedAdvertisement.get().getTitle(), is(AD_BIKE.getTitle()));
    assertThat(returnedAdvertisement.get().getContact(), is(AD_BIKE.getContact()));
    assertThat(returnedAdvertisement.get().getPrice(), is(BigDecimal.ONE));
    assertThat(returnedAdvertisement.get().getCurrency(), is("USD"));

  }*/

/*  @Test
  public void testUpdateTitleOfExistingAdvertisement() {
    Advertisement returnedAdvertisement = adStorage.save(AD_BIKE);

    final String newTitle = "Bruce Schneier";
    returnedAdvertisement.setTitle(newTitle);

    adStorage.save(returnedAdvertisement);

    assertThat(returnedAdvertisement.getTitle(), is(newTitle));

  }*/

  @Test
  public void testUpdatePriceOfExistingAdvertisement() {
    Advertisement returnedAdvertisement = adStorage.save(AD_BIKE);

    final BigDecimal newPrice = BigDecimal.valueOf(2L);
    returnedAdvertisement.setPrice(newPrice);

    adStorage.save(returnedAdvertisement);

    assertThat(returnedAdvertisement.getPrice(), is(newPrice));

  }

  @Test
  public void testGetAllEmpty() {
    List<Advertisement> returnedAdvertisements = adStorage.findAll();
    assertThat(returnedAdvertisements.size(), is(0));
  }

  @Test
  public void testGetAllFirstOneThenTwoEntries() {

    adStorage.save(AD_BIKE);

    List<Advertisement> returnedAdvertisements = adStorage.findAll();
    assertThat(returnedAdvertisements.size(), is(1));
    Advertisement ad = returnedAdvertisements.get(0);
    assertThat(ad.getTitle(), is(AD_BIKE.getTitle()));
    assertThat(ad.getContact(), is(AD_BIKE.getContact()));
    assertThat(ad.getCurrency(), is("USD"));

    adStorage.save(AD_PAN);

    returnedAdvertisements = adStorage.findAll();
    assertThat(returnedAdvertisements.size(), is(2));

  }

/*  @Test
  public void testRetrieveAdvertisementByIdThrowsExceptionForNegativeValue() {

    assertThatThrownBy(() ->
    {
      adStorage.findById(-1L);
    }).isInstanceOf(IllegalArgumentException.class);

  }*/

/*  @Test
  public void testDeleteSingle() {

    adStorage.save(AD_BIKE);
    adStorage.save(AD_PAN);

    List<Advertisement> returnedAdvertisements = adStorage.findAll();
    assertThat(returnedAdvertisements.size(), is(2));

    adStorage.deleteById(1L);

    returnedAdvertisements = adStorage.findAll();

    assertThat(returnedAdvertisements.size(), is(1));
    Advertisement ad = returnedAdvertisements.get(0);
    assertThat(ad.getTitle(), is(AD_PAN.getTitle()));
    assertThat(ad.getContact(), is(AD_PAN.getContact()));
    assertThat(ad.getId(), is(2L));

  }*/

  @Test
  public void testDeleteAll() {

    adStorage.save(AD_BIKE);
    adStorage.save(AD_PAN);

    List<Advertisement> returnedAdvertisements = adStorage.findAll();
    assertThat(returnedAdvertisements.size(), is(2));

    adStorage.deleteAll();

    returnedAdvertisements = adStorage.findAll();
    assertThat(returnedAdvertisements.size(), is(0));
  }

  private Advertisement createAdvertisementWithTitleContactPriceCurrency(
    String title, String contact, BigDecimal price, String currency) {
    Advertisement ad = new Advertisement();
    ad.setTitle(title);
    ad.setContact(contact);
    ad.setPrice(price);
    ad.setCurrency(currency);
    return ad;
  }

}