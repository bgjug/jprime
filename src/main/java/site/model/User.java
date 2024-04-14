package site.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.HashSet;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "UNQ_EMAIL", columnNames = {"email"}))
public class User extends AbstractEntity {

    /**
     * Default serial version uid.
     */
    private static final long serialVersionUID = 1L;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    @NotBlank
    @Email
    private String email;

    @JsonIgnore
    private String password;

    @Transient
    @JsonIgnore
    private String cpassword;

    @JsonIgnore
    private String phone;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY, targetEntity = Article.class)
    @JsonIgnore
    private Collection<Article> articles = new HashSet<>();

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
        if (this == o) {
            return true;
        }
        if (!(o instanceof User user)) {
            return false;
        }

        if (email != null && !email.equals(user.email)) {
            return false;
        }
        if (firstName != null && !firstName.equals(user.firstName)) {
            return false;
        }
        return !(lastName != null && !lastName.equals(user.lastName));
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

    @Override
    public String toString() {
        return "User [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
    }

    public void setCpassword(String cpassword) {
        this.cpassword = cpassword;
    }
}
