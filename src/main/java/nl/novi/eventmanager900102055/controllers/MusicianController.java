package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.dtos.MusicianDto;
import nl.novi.eventmanager900102055.models.Musician;
import nl.novi.eventmanager900102055.repositories.MusicianRepository;
import nl.novi.eventmanager900102055.services.MusicianService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/musicians")
public class MusicianController {

    private final MusicianService MusicianService;

    public MusicianController(MusicianService MusicianService){
        this.MusicianService = MusicianService;

    }

    @GetMapping
    public ResponseEntity<List<MusicianDto>> getAllMusicians() {
        List<MusicianDto> musicianDtoList;
        musicianDtoList = MusicianService.getAllMusicians();
        return ResponseEntity.ok().body(musicianDtoList);
    }

    @GetMapping("/lastname")
    public ResponseEntity<List<MusicianDto>> getMusicianByLastName(@RequestParam String lastname) {
        List<MusicianDto> musicianDtoList;
        musicianDtoList = MusicianService.getMusicianByLastName(lastname);
        return ResponseEntity.ok().body(musicianDtoList);
    }

    @PostMapping
    public ResponseEntity<Object> addMusician(@RequestBody MusicianDto musicianDto) {
        MusicianDto dto = MusicianService.addMusician(musicianDto);
        URI location = UriComponentsBuilder
                .fromPath("/musicians/{lastName}")
                .buildAndExpand(dto.lastName)
                .toUri();

        return ResponseEntity.created(location).body(dto);  }

}
