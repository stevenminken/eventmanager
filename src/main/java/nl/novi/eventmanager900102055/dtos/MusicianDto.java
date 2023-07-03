package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.NotBlank;

public class MusicianDto {
//
//    Hier kunnen annotaties bij Max(100), @Past, size(nin= 1, max = 35, message = 'groottesdfasdf'), @notBlank
//
    public Long id;
    @NotBlank
    public String firstName;
    @NotBlank
    public String lastName;

}
