package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.User;

import java.util.List;

public class ArtistDto {

    private Long id;
    @NotEmpty
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
