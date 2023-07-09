package nl.novi.eventmanager900102055.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String address;
    private String email;
    private Integer numberOfSeats;
    @ManyToOne
    private User user;
    @ManyToMany(mappedBy = "locationList")
    private List<Event> eventList;

    public Location() {
    }

    public Location(String name, String address, String email, Integer numberOfSeats) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.numberOfSeats = numberOfSeats;
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEvents(List<Event> eventList) {
        this.eventList = eventList;
    }

    public String getDetails() {
        return "details";
    }
}
