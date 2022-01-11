package rewardsdining.reward;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import common.money.MonetaryAmount;
import rewardsdining.account.AccountContribution;
import rewardsdining.restaurant.Dining;
import rewardsdining.restaurant.Restaurant;

/**
 * JDBC implementation of a reward repository that records the result of a
 * reward transaction by inserting a reward confirmation record.
 */
@Repository
public class JdbcRewardRepository implements RewardRepository {

	private static final String FIND_REWARD_SQL ="select r.ID, r.CONFIRMATION_NUMBER, r.REWARD_AMOUNT, r.REWARD_DATE, r.ACCOUNT_NUMBER, r.DINING_MERCHANT_NUMBER, r.DINING_DATE, r.DINING_AMOUNT, rt.MERCHANT_NUMBER, rt.NAME from T_REWARD r inner join T_RESTAURANT rt on (r.DINING_MERCHANT_NUMBER = rt.MERCHANT_NUMBER)";
	
	private final JdbcTemplate jdbcTemplate;

	public JdbcRewardRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public RewardConfirmation confirmReward(AccountContribution contribution, Dining dining) {
		String sql = "insert into T_REWARD (CONFIRMATION_NUMBER, REWARD_AMOUNT, REWARD_DATE, ACCOUNT_NUMBER, DINING_MERCHANT_NUMBER, DINING_DATE, DINING_AMOUNT) values (?, ?, ?, ?, ?, ?, ?)";
		String confirmationNumber = nextConfirmationNumber();
		jdbcTemplate.update(sql, confirmationNumber, contribution.getAmount().asBigDecimal(),
				Date.valueOf(LocalDate.now()), contribution.getAccountNumber(), dining.getMerchantNumber(),
				Date.valueOf(dining.getDate()), dining.getAmount().asBigDecimal());
		return new RewardConfirmation(confirmationNumber, contribution);
	}
	
	public List<Reward> findByAccountNumber(String accountNumber) {
		return jdbcTemplate.query(FIND_REWARD_SQL + " where ACCOUNT_NUMBER = ? order by r.DINING_DATE desc", this::mapDining, accountNumber);
	}
	
	public List<Reward> findAll() {
		return jdbcTemplate.query(FIND_REWARD_SQL + " order by r.DINING_DATE desc", this::mapDining);
	}

	private String nextConfirmationNumber() {
		String sql = "select next value for S_REWARD_CONFIRMATION_NUMBER from DUAL_REWARD_CONFIRMATION_NUMBER";
		return jdbcTemplate.queryForObject(sql, String.class);
	}
	
	private Reward mapDining(ResultSet rs, int rowNum) throws SQLException {
		Reward reward = new Reward();
		reward.setId(rs.getLong("ID"));
		reward.setConfirmationNumber(rs.getString("CONFIRMATION_NUMBER"));
		reward.setAmount(new MonetaryAmount(rs.getDouble("REWARD_AMOUNT")));
		reward.setRewardDate(rs.getDate("REWARD_DATE").toLocalDate());
		reward.setAccountNumber(rs.getString("ACCOUNT_NUMBER"));
		reward.setDiningDate(rs.getDate("DINING_DATE").toLocalDate());
		reward.setDiningAmount(new MonetaryAmount(rs.getDouble("DINING_AMOUNT")));
		Restaurant restaurant = new Restaurant(rs.getString("MERCHANT_NUMBER"), rs.getString("NAME"));
		reward.setRestaurant(restaurant);
		return reward;
	}
}