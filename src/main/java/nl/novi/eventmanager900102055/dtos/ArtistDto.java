package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import nl.novi.eventmanager900102055.models.Event;

import java.util.List;

public class ArtistDto {

    private Long id;
    @Pattern(regexp = "^[^0-9]+$", message = "Name cannot have digits")
    @Size(min = 2, message = "name artist should have at least 2 characters")
    private String name;
    @NotEmpty
    private String genre;
    private List<Event> eventList;

    public ArtistDto() {
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

    public String getGenre() {
        return genre;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

}
