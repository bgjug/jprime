package site.model;

import java.util.Arrays;

import jakarta.persistence.Entity;
import jakarta.persistence.Lob;

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
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof VenueHall venueHall)) {
            return false;
        }

		if (description == null) {
            if (venueHall.description != null) {
                return false;
            }
		} else if (!description.equals(venueHall.description)) {
            return false;
        }
        if (!Arrays.equals(map, venueHall.map)) {
            return false;
        }

		if (name == null) {
            return venueHall.name == null;
		} else
            return name.equals(venueHall.name);
    }
}
