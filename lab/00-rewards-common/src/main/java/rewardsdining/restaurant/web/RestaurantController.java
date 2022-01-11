package rewardsdining.restaurant.web;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import rewardsdining.restaurant.RestaurantManager;

@Controller
@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
public class RestaurantController {

	private final RestaurantManager restaurantManager;
	
	
	public RestaurantController(RestaurantManager restaurantManager) {
		this.restaurantManager = restaurantManager;
	}

	@GetMapping("/restaurants")
	public String listRestaurants(Model model) {
		model.addAttribute("restaurants", restaurantManager.findAll());
		return "restaurant/list";
	}
}
