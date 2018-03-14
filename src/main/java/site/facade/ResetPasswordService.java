package site.facade;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.token.Sha512DigestUtils;
import org.springframework.stereotype.Service;

import site.model.ResetPasswordToken;
import site.model.User;
import site.repository.ResetPasswordTokenRepository;

/**
 * @author Zhorzh Raychev
 */
@Service()
public class ResetPasswordService {

	@Value("${site.reset.password.token.duration.hours:2}")
	private int TOKEN_DURATION_IN_HOURS;

	private static final Logger logger = LogManager.getLogger(ResetPasswordService.class);

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
			logger.debug("ResetPasswordToken id=" + tokenId + " , ShaHex: " + tokenShaHex
					+ " NOT found. This could be an attacker brute forcing the token!");
			return null;
		}
		User owner = resetPasswordToken.getOwner();
		if (resetPasswordToken.isUsed()) {
			logger.debug("ResetPassworToken for user: " + owner + " with Id=" + tokenId + ", ShaHex: " + tokenShaHex
					+ " is aleady used.");
			return null;
		}

		DateTime createdDate = resetPasswordToken.getCreatedDate();
		DateTime deadline = createdDate.plusHours(TOKEN_DURATION_IN_HOURS);
		if (deadline.isBeforeNow()) {
			logger.debug("ResetPassworToken for user: " + owner + " with Id=" + tokenId + ", ShaHex: " + tokenShaHex
					+ "  is expired.");
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
		String output = sb.toString();
		return output;
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
