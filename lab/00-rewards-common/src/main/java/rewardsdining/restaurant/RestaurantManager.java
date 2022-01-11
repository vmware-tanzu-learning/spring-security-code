package rewardsdining.restaurant;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RestaurantManager {

	private final RestaurantRepository restaurantRepository;
	
	
	public RestaurantManager(RestaurantRepository restaurantRepository) {
		this.restaurantRepository = restaurantRepository;
	}

	@Transactional(readOnly = true)
	public Optional<Restaurant> findById(long restaurantId) {
		return restaurantRepository.findById(restaurantId);
	}
	
	@Transactional(readOnly = true)
	public List<Restaurant> findAll() {
		return restaurantRepository.findAll();
	}
	
	public Restaurant save(Restaurant restaurant) {
		return restaurantRepository.save(restaurant);
	}
}