package site.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import site.model.ResetPasswordToken;
import site.model.User;

/**
 * @author Zhorzh Raychev
 */
@Repository(value = ResetPasswordTokenRepository.NAME)
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

	String NAME = "resetPasswordTokenRepository";
	
	ResetPasswordToken findByTokenId(String tokenId);

	List<ResetPasswordToken> findAllByOwner(User owner);
}
