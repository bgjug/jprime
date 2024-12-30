package site.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;

/**
 * @author Zhorzh Raychev
 */
@Entity
@Table(name = "ResetPasswordToken", uniqueConstraints = @UniqueConstraint(name = "UNQ_TOKEN", columnNames = {"tokenId"}))
public class ResetPasswordToken extends AbstractEntity {

	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id", foreignKey = @ForeignKey(name="fk_token_user"))
	private User owner;
	
	@NotBlank
	private String tokenId;
	
	private boolean used;

	public ResetPasswordToken() {
		
	}
	
	public ResetPasswordToken(User owner, String tokenId) {
		this.owner = owner;
		this.tokenId = tokenId;
		setCreatedDate(LocalDateTime.now());
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}
	
	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((tokenId == null) ? 0 : tokenId.hashCode());
		result = prime * result + (used ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ResetPasswordToken passwordToken)) {
			return false;
		}

		if (owner == null) {
            if (passwordToken.owner != null) {
                return false;
            }
		} else if (!owner.equals(passwordToken.owner)) {
            return false;
        }
		if (tokenId == null) {
            if (passwordToken.tokenId != null) {
                return false;
            }
		} else if (!tokenId.equals(passwordToken.tokenId)) {
            return false;
        }
        return used == passwordToken.used;
	}

	@Override
	public String toString() {
		return "ResetPasswordToken [owner=" + owner + ", tokenId=" + tokenId + ", used=" + used + "]";
	}
	
}
