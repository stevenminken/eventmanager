package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.models.Musician;
import nl.novi.eventmanager900102055.repositories.MusicianRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/musicians")
public class MusicianController {
    @Autowired
    private MusicianRepository repos;

    @GetMapping
    public ResponseEntity<Iterable<Musician>> getMusicians() {
        return ResponseEntity.ok(repos.findAll());
    }

    @PostMapping
    public ResponseEntity<Musician> createMusician(@RequestBody Musician m) {
        repos.save(m);
        URI uri = URI.create(ServletUriComponentsBuilder
                .fromCurrentRequest().path("/" + m.getId()).toUriString());
        return ResponseEntity.created(uri).body(m);
    }

    @GetMapping("/lastname")
    public ResponseEntity<Iterable<Musician>> getMusicianByLastName(@RequestParam String lastname) {
        return ResponseEntity.ok(repos.findByLastName(lastname));
    }
}
