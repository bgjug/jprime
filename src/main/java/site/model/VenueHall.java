package site.model;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.Lob;

@Entity
public class VenueHall extends AbstractEntity {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	
	private String description;
	
    @Lob
    private byte[] map;

	public VenueHall() {
	}

	public VenueHall(String name, String description) {
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getMap() {
		return map;
	}

	public void setMap(byte[] map) {
		this.map = map;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + Arrays.hashCode(map);
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		VenueHall other = (VenueHall) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (!Arrays.equals(map, other.map))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
