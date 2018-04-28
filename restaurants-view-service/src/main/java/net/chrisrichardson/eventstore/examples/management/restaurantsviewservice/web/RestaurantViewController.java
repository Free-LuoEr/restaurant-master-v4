package net.chrisrichardson.eventstore.examples.management.restaurantsviewservice.web;

import net.chrisrichardson.eventstore.examples.management.restaurant.common.Address;
import net.chrisrichardson.eventstore.examples.management.restaurant.common.DeliveryTime;
import net.chrisrichardson.eventstore.examples.management.restaurant.common.FindAvailableRestaurantsRequest;
import net.chrisrichardson.eventstore.examples.management.restaurant.common.RestaurantInfo;
import net.chrisrichardson.eventstore.examples.management.restaurantsviewservice.backend.redis.RestaurantQuerySideRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/restaurants")
public class RestaurantViewController {

  @Autowired
  private RestaurantQuerySideRedisService restaurantQuerySideRedisService;
  
  int queriesCount=0;

  @RequestMapping(value = "/{id}", method = RequestMethod.GET)
  public ResponseEntity<RestaurantInfo> findRestaurant(@PathVariable("id") String restaurantId) {
  
  queriesCount=queriesCount+1;
  	
  if( (queriesCount==3) || (queriesCount==6) || (queriesCount==9) ){
     return  new ResponseEntity<>(HttpStatus.NOT_FOUND);
  	}
   
  else{
     RestaurantInfo r = restaurantQuerySideRedisService.findById(restaurantId);
     return r == null ?  new ResponseEntity<>(HttpStatus.NOT_FOUND) : ResponseEntity.ok(r);
  	}

 }

  @RequestMapping(value = "/availablerestaurants", method = RequestMethod.GET)
  @ResponseBody
  public ResponseEntity<List<RestaurantInfo>> findAvailableRestaurants(@ModelAttribute FindAvailableRestaurantsRequest request) {
    Address address = request.makeAddress();
    DeliveryTime deliveryTime = request.makeDeliveryTime();
    return ResponseEntity.ok(restaurantQuerySideRedisService.findAvailableRestaurants(address, deliveryTime));
  }
}
