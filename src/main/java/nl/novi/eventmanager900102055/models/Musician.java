package nl.novi.eventmanager900102055.models;

import jakarta.persistence.*;

@Entity
@Table(name="musicians")
public class Musician {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    public Musician() {}
    public Musician(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
}