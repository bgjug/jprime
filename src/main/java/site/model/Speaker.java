package site.model;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Lob;

@Entity
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class Speaker extends User {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String bio;
	
	private String headline;
	
	private String twitter;
	
	@Lob
	private byte[] picture;


	public byte[] getPicture() {
		return picture;
	}


	public void setPicture(byte[] picture) {
		this.picture = picture;
	}


	public String getHeadline() {
		return headline;
	}


	public void setHeadline(String headline) {
		this.headline = headline;
	}


	public String getBio() {
		return bio;
	}


	public void setBio(String bio) {
		this.bio = bio;
	}


	public String getTwitter() {
		return twitter;
	}


	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

}
