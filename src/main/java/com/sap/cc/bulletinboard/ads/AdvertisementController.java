package com.sap.cc.bulletinboard.ads;

import com.sap.cc.bulletinboard.NotFoundException;
import com.sap.cc.bulletinboard.rating.AverageRatingServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/ads")
public class AdvertisementController {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private AdvertisementRepository  advertisementStorage ;
  private AverageRatingServiceClient averageRatingServiceClient;

  private FetchAverageRatingCreator fetchAverageRatingCreator;
  @Autowired
  public AdvertisementController(AdvertisementRepository advertisementStorage,FetchAverageRatingCreator fetchAverageRatingCreator) {
    this.advertisementStorage = advertisementStorage;
    this.fetchAverageRatingCreator=fetchAverageRatingCreator;
  }

  @GetMapping
  public List<Advertisement> getAllAds(){
    return advertisementStorage.findAll();
  }

  @PostMapping
  public ResponseEntity<Advertisement> createdAd(@RequestBody Advertisement ad) throws URISyntaxException {
    Advertisement newAd=createdBook(null, ad.getTitle(),ad.getContact(),ad.getPrice(),ad.getCurrency());
    UriComponents uriComponents = UriComponentsBuilder
      .fromPath("/api/v1/ads" + "/{id}")
      .buildAndExpand(newAd.getId());
    URI locationHeaderUri = new URI(uriComponents.getPath());
    return ResponseEntity.created(locationHeaderUri).body(newAd);
  }

  private Advertisement createdBook(Long id,String title,String contact, BigDecimal price,String currency){
    Advertisement ad=new Advertisement(title);
    ad.setId(id);
    ad.setContact(contact);
    ad.setPrice(price);
    ad.setCurrency(currency);
    ad.setAverageContactRating(fetchAverageRatingCreator.getAverageRating(ad));
    ad=advertisementStorage.save(ad);
    return ad;
  }

  @GetMapping(path = "/{id}")
  public ResponseEntity<Advertisement> getSingleAd(@PathVariable("id") Long id) throws IllegalArgumentException{
    try {
      MDC.put("path", "api/v1/ads/" + id);
      if (id <= 0) {
        IllegalArgumentException illegalArgumentException = new IllegalArgumentException("Id must not be less than 1");
        logger.warn("Call stack of the IllegalArgumentException " + illegalArgumentException);
        throw illegalArgumentException;
      }
      if (advertisementStorage.findById(id).isPresent()) {
        logger.info("ID of the requested advertisement {}", id);
        Advertisement adAsResponse = advertisementStorage.findById(id).get();
        logger.trace("String representation of the advertisement {}", adAsResponse.toString());
        return ResponseEntity.ok(adAsResponse);
      }
      throw new NotFoundException();
    }finally{
      MDC.clear();
    }
  }
}
