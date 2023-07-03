package nl.novi.eventmanager900102055.controllers;

import jakarta.validation.Valid;
import nl.novi.eventmanager900102055.dtos.MusicianDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.services.MusicianService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/musicians")
public class MusicianController {

    private final MusicianService MusicianService;

    public MusicianController(MusicianService MusicianService) {
        this.MusicianService = MusicianService;

    }

    @GetMapping
    public ResponseEntity<List<MusicianDto>> getAllMusicians() {
        List<MusicianDto> musicianDtoList;
        musicianDtoList = MusicianService.getAllMusicians();
        return ResponseEntity.ok().body(musicianDtoList);
    }

    @GetMapping("/lastname")
    public ResponseEntity<List<MusicianDto>> getMusicianByLastName(@RequestParam String lastname) throws ResourceNotFoundException {
        List<MusicianDto> musicianDtoList;
        musicianDtoList = MusicianService.getMusicianByLastName(lastname);
        return ResponseEntity.ok().body(musicianDtoList);
    }

    // betere terugkoppeling request
    @PostMapping
    public ResponseEntity<Object> addMusician(@Valid @RequestBody MusicianDto musicianDto, BindingResult br) throws NameDuplicateException {
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("/n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            MusicianDto dto = MusicianService.addMusician(musicianDto);
            URI location = UriComponentsBuilder
                    .fromPath("/musicians/{lastName}")
                    .buildAndExpand(dto.lastName)
                    .toUri();

            return ResponseEntity.created(location).body(dto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteMusician(@PathVariable("id") Long id) {
        boolean deleted = MusicianService.deleteMusician(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.badRequest().body("The id you are trying to delete does not exist.");
        }
    }
}
