package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.services.ArtistService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

//    @GetMapping(value = "/{id}")
//    public ResponseEntity<Object> getArtist(@PathVariable("id") long id) {
//        return ResponseEntity.ok().body(ArtistService.getArtistById(id));
//    }

}
