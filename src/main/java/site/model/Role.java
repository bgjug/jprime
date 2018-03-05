package site.model;

import javax.persistence.Entity;

import org.springframework.security.core.GrantedAuthority;

/**
 * @author Trifon Trifonov
 */
@Entity
public class Role 
	extends AbstractEntity
	implements GrantedAuthority
{

	private static final long serialVersionUID = 1L;

	public final static String USER_NAME = "USER";
	public final static String ADMIN_NAME = "ADMIN";
	
	private String name;

	public Role() {
		
	}
	public Role(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return name;
	}

	@Override
	public String toString() {
		return "Role ["+
				"id=" + getId() +
				", name=" + name +
				"]";
	}
}