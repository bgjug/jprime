package site.model;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;


/**
 * @author Zhorzh Raychev
 */
@Entity
@Table(name = "ResetPasswordToken")
public class ResetPasswordToken extends AbstractEntity {

	private static final long serialVersionUID = -3626660176280247512L;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", referencedColumnName = "id")
	private User owner;
	
	@Column(unique = true)
    @NotBlank
	private String tokenId;
	
	private boolean used = false;

	public ResetPasswordToken() {
		
	}
	
	public ResetPasswordToken(User owner, String tokenId) {
		this.owner = owner;
		this.tokenId = tokenId;
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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ResetPasswordToken other = (ResetPasswordToken) obj;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (tokenId == null) {
			if (other.tokenId != null)
				return false;
		} else if (!tokenId.equals(other.tokenId))
			return false;
		if (used != other.used)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ResetPasswordToken [owner=" + owner + ", tokenId=" + tokenId + ", used=" + used + "]";
	}
	
}
