package rewardsdining.account.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import rewardsdining.account.Account;

/**
 * Loads account aggregates. Called by the reward network to find and reconstitute Account entities from an external
 * form such as a set of RDMS rows.
 * 
 * Objects returned by this repository are guaranteed to be fully-initialized and ready to use.
 */
public interface AccountRepository extends JpaRepository<Account,Long>, CustomAccountRepository {

	Optional<Account> findByUsername(String username);
}