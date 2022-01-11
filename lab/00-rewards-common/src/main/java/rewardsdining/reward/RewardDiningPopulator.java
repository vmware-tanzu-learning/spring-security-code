package rewardsdining.reward;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import rewardsdining.restaurant.Dining;
import rewardsdining.restaurant.Restaurant;
import rewardsdining.restaurant.RestaurantRepository;

@Component
public class RewardDiningPopulator implements CommandLineRunner {

	private final double MAX_DINING_AMOUNT = 150;
	
	private final int REWARDS_PER_ACCOUNT = 5;
	
	private final RewardNetwork rewardNetwork;
	
	private final RestaurantRepository restaurantRepository;

	private final List<String> creditCards = List.of("1234123412341234", "1234123412340003", 
			"1234123412340008", "1234123412340012", "1234123412340015");
	

	public RewardDiningPopulator(RewardNetwork rewardNetwork, RestaurantRepository restaurantRepository) {
		this.rewardNetwork = rewardNetwork;
		this.restaurantRepository = restaurantRepository;
	}


	@Override
	public void run(String... args) throws Exception {
		var restaurants = restaurantRepository.findAll();

		for(String creditCard : creditCards) {
			for(int i = 0; i < REWARDS_PER_ACCOUNT; i++) {
				generateRewardFor(creditCard, getRandomRestaurant(restaurants).getNumber());
			}
		}
	}
	
	private void generateRewardFor(String creditCard, String merchantNumber) {
		
		var now = LocalDate.now();
		
		var past = LocalDate.now().minusMonths(1);
		
		var rewardDate = generateRandomDate(now, past);
		
		var dining = Dining.createDining(getRandomAmount(), creditCard, merchantNumber , rewardDate.getMonthValue(), rewardDate.getDayOfMonth(), rewardDate.getYear());
		
		rewardNetwork.rewardAccountFor(dining);
	}
	
	private LocalDate generateRandomDate(LocalDate now, LocalDate past) {
		return LocalDate.ofEpochDay(ThreadLocalRandom.current()
                .nextLong(past.toEpochDay(), now.toEpochDay()));
	}
	
	private Restaurant getRandomRestaurant(List<Restaurant> restaurants) {
		return restaurants.get(ThreadLocalRandom.current().nextInt(restaurants.size()));
	}
	
	private String getRandomAmount() {
		return String.valueOf(ThreadLocalRandom.current().nextDouble() * MAX_DINING_AMOUNT);
	}


	
}
