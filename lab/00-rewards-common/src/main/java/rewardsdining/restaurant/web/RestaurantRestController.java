package rewardsdining.restaurant.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import common.money.Percentage;
import rewardsdining.restaurant.Restaurant;
import rewardsdining.restaurant.RestaurantManager;

@RestController
public class RestaurantRestController {

	private final RestaurantManager restaurantManager;
	
	
	public RestaurantRestController(RestaurantManager restaurantManager) {
		this.restaurantManager = restaurantManager;
	}

	@GetMapping("/restaurants")
	public List<Restaurant> listRestaurants() {
		return restaurantManager.findAll();
	}
	
	@GetMapping("/restaurants/{id}")
	public ResponseEntity<Restaurant> restaurantDetails(@PathVariable long id) {
		return restaurantManager.findById(id)
				.map(ResponseEntity::ok)
				.orElseGet(() -> ResponseEntity.notFound().build());
	}
	
	@PutMapping("/restaurants/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void updateRestaurant(@PathVariable long id, @RequestBody @Valid RestaurantDto restaurantDto) {
		restaurantManager.findById(id)
			.ifPresent(restaurant -> {
				restaurant.setName(restaurantDto.getName());
				restaurant.setLocation(restaurantDto.getLocation());
				restaurant.setBenefitPercentage(new Percentage(restaurantDto.getBenefitPercentage()));
				restaurantManager.save(restaurant);
			});
	}
}
