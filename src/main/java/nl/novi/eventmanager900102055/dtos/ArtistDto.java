package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.NotNull;

public class ArtistDto {

    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String genre;

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

    public void setGenre(String genre) {
        this.genre = genre;
    }
}
