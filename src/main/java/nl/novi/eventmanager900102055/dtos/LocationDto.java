package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import nl.novi.eventmanager900102055.models.Event;

import java.util.List;

public class LocationDto {
    private Long id;
    @Size(min = 2, message = "Location name should have at least 2 characters")
    private String name;

    private String address;
    @Email(message = "Invalid email format")
    private String email;
    @Min(10)
    private Integer numberOfSeats;

    private List<Event> eventList;

    public LocationDto() {
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

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }
}
