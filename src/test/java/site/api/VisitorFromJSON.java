package site.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
class VisitorFromJSON {

    final String ticket;

    final String registrantName;

    final String name;

    final String email;

    final String company;

    final boolean registered;

    @JsonCreator
    public VisitorFromJSON(@JsonProperty("ticket") String ticket,
        @JsonProperty("registrantName") String registrantName, @JsonProperty("name") String name,
        @JsonProperty("email") String email, @JsonProperty("company") String company,
        @JsonProperty("registered") boolean registered) {
        this.ticket = ticket;
        this.registrantName = registrantName;
        this.name = name;
        this.email = email;
        this.company = company;
        this.registered = registered;
    }

    public String getTicket() {
        return ticket;
    }

    public String getRegistrantName() {
        return registrantName;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCompany() {
        return company;
    }

    public boolean isRegistered() {
        return registered;
    }
}