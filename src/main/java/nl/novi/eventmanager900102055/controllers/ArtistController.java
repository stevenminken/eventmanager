package nl.novi.eventmanager900102055.controllers;

import jakarta.validation.Valid;
import nl.novi.eventmanager900102055.dtos.ArtistDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.services.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService ArtistService;

    public ArtistController(ArtistService ArtistService) {

        this.ArtistService = ArtistService;
    }

    @GetMapping
    public ResponseEntity<List<ArtistDto>> findAllArtists() {
        List<ArtistDto> ArtistDtoList;
        ArtistDtoList = ArtistService.findAllArtists();
        return ResponseEntity.ok().body(ArtistDtoList);
    }

    @GetMapping(params = "id")
    public ResponseEntity<Object> findArtistById(@RequestParam("id") Long id) {
        try {
            ArtistDto ArtistDto = ArtistService.findArtistById(id);
            return ResponseEntity.ok().body(ArtistDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the artist: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    //    http://localhost:8080/artists?name=jan
    @GetMapping(params = "name")
    public ResponseEntity<Object> findArtistByName(@RequestParam("name") String name) {
        try {
            ArtistDto ArtistDto = ArtistService.findArtistByName(name);
            return ResponseEntity.ok().body(ArtistDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the artist: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping("/{userId}")
    public ResponseEntity<Object> createArtist(@Valid @RequestBody ArtistDto ArtistDto, BindingResult bindingResult, @PathVariable("userId") long userId) throws NameDuplicateException {

        if (bindingResult.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            ArtistDto dto = ArtistService.createArtist(ArtistDto, userId);
            URI location = UriComponentsBuilder
                    .fromPath("/Artists/{lastName}")
                    .buildAndExpand(dto.getName())
                    .toUri();

            return ResponseEntity.created(location).body("Artist created: " + dto.getName() + " with id: " + +dto.getId());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateArtist(@PathVariable("id") Long id, @Valid @RequestBody ArtistDto ArtistDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("/n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            ArtistDto updatedArtistDto = ArtistService.updateArtist(id, ArtistDto);
            return ResponseEntity.ok().body(updatedArtistDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable("id") Long id) {
        boolean deleted = ArtistService.deleteArtist(id);
        if (deleted) {
            return ResponseEntity.ok().body("Artist deleted");
        } else {
            return ResponseEntity.badRequest().body("Artist not deleted");
        }
    }

}
