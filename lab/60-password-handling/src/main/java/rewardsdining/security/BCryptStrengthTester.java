package rewardsdining.security;

import java.time.Duration;
import java.time.Instant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BCryptStrengthTester {
	
	private static final Logger logger = LoggerFactory.getLogger(BCryptStrengthTester.class);

	
	private static final int TARGET_HASHING_TIME = 1000;
	
	private static final int MIN_STRENGHT = 4;
	
	private static final int MAX_STRENGHT = 31;

	private static final String PASSWORD = "s3cureP4ssword#";
	
	
	public static int startTest() {
		int strenght = MIN_STRENGHT - 1;
		Duration timeElapsed = Duration.ZERO;
		
		while(++strenght < MAX_STRENGHT && timeElapsed.toMillis() < TARGET_HASHING_TIME) {
			var bcrypt = new BCryptPasswordEncoder(strenght);
			
			Instant start = Instant.now();
			bcrypt.encode(PASSWORD);
			timeElapsed = Duration.between(start, Instant.now()); 
			
			logger.info("BCrypt with strenght {} took {} millis", strenght, timeElapsed.toMillis());
		}
		
		return strenght;
	}
}
