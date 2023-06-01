package site.facade;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.model.ResetPasswordToken;
import site.model.User;
import site.repository.ResetPasswordTokenRepository;

/**
 * @author Zhorzh Raychev
 */
@Service()
public class ResetPasswordService {

	@Value("${site.reset.password.token.duration.hours:2}")
	private int tokenDurationInHours;

	private static final Logger logger = LoggerFactory.getLogger(ResetPasswordService.class);

	@Autowired
	private ResetPasswordTokenRepository resetPassRepository;

	public String createNewToken(User user) {
		String tokenId = getNewTokenId();
		ResetPasswordToken resetPassToken = new ResetPasswordToken(user, tokenId);
		String tokenShaHex = Sha512DigestUtils.shaHex(resetPassToken.getTokenId());
		resetPassToken.setTokenId(tokenShaHex);
		resetPassRepository.save(resetPassToken);

		return tokenId;
	}

	/**
	 * @param tokenId
	 * @return User owning the token if tokenId is valid, return NULL if tokenId
	 *         is not valid
	 */
	public User checkTokenValidity(String tokenId) {

		String tokenShaHex = Sha512DigestUtils.shaHex(tokenId);
		ResetPasswordToken resetPasswordToken = resetPassRepository.findByTokenId(tokenShaHex);
		if (resetPasswordToken == null) {
			logger.debug("ResetPasswordToken id={} , ShaHex: {} NOT found. This could be an attacker brute forcing the token!", tokenId, tokenShaHex);
			return null;
		}
		User owner = resetPasswordToken.getOwner();
		if (resetPasswordToken.isUsed()) {
			logger.debug("ResetPassworToken for user: {} with Id={}, ShaHex: {} is aleady used.", owner, tokenId, tokenShaHex);
			return null;
		}

		DateTime createdDate = resetPasswordToken.getCreatedDate();
		DateTime deadline = createdDate.plusHours(tokenDurationInHours);
		if (deadline.isBeforeNow()) {
			logger.debug("ResetPassworToken for user: {} with Id={}, ShaHex: {}  is expired.", owner, tokenId, tokenShaHex);
			return null;
		}
		return owner;
	}

	public void setTokenToUsed(String tokenId) {

		String tokenShaHex = Sha512DigestUtils.shaHex(tokenId);
		ResetPasswordToken resetPasswordToken = resetPassRepository.findByTokenId(tokenShaHex);
		User owner = resetPasswordToken.getOwner();
		List<ResetPasswordToken> tokens = resetPassRepository.findAllByOwner(owner);
		for (ResetPasswordToken token : tokens) {
			token.setUsed(true);
		}
		resetPassRepository.saveAll(tokens);
	}

	private String getNewTokenId() {
		SecureRandom random = getRandom();
		char[] chars = "abbbcdefghhiiijklmmnopqrstuuvwxyzz1234567899990".toCharArray();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 128; i++) {
			char c = chars[random.nextInt(chars.length)];
			sb.append(c);
		}
		return sb.toString();
	}

	private SecureRandom getRandom() {
		SecureRandom random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG", "SUN");
		} catch (NoSuchAlgorithmException | NoSuchProviderException e) {
			random = new SecureRandom();
		}
		random.nextBytes(new byte[256]);

		return random;
	}

}
