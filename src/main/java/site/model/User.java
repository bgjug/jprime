package site.model;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

@Entity
public class User extends AbstractEntity {
	/**
	 * Default serial version uid.
	 */
	private static final long serialVersionUID = 1L;

	public static final String ADMIN_EMAIL = "admin@jsprime.io";

	private String firstName;

	private String lastName;

	@Column(unique = true)
	@NotBlank
	@Email
	private String email;

	private String password;

	@Transient
	private String cpassword;

	private String phone;

	@OneToMany(mappedBy = "author", fetch = FetchType.LAZY, targetEntity = Article.class)
	private Collection<Article> articles = new HashSet<>();

	//@Trifon
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role"
		, joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id")
		, inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") )
	private Collection<Role> roles = new HashSet<>();


	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<Article> getArticles() {
		return articles;
	}

	public void setArticles(Collection<Article> articles) {
		this.articles = articles;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof User))
			return false;

		User user = (User) o;

		if (email != null && !email.equals(user.email))
			return false;
		if (firstName != null && !firstName.equals(user.firstName))
			return false;
		if (lastName != null && !lastName.equals(user.lastName))
			return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = firstName.hashCode();
		result = 31 * result + lastName.hashCode();
		result = 31 * result + email.hashCode();
		return result;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCpassword() {
		return cpassword;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	public void addRole(Role role) {
		if (role == null) {
			throw new IllegalArgumentException("Role MUST not be null!");
		}
		roles.add(role);
	}

	@Override
	public String toString() {
		return "User [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}

	public void setCpassword(String cpassword) {
		this.cpassword = cpassword;
	}
}