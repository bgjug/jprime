package site.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VisitorSearch {

    private final String email;

    private final String firstName;

    private final String lastName;

    private final String company;

    @JsonCreator
    public VisitorSearch(@JsonProperty("email") String email, @JsonProperty("firstName") String firstName,
        @JsonProperty("lastName") String lastName, @JsonProperty("company") String company) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
