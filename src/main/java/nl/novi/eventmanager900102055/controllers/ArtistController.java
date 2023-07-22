package nl.novi.eventmanager900102055.controllers;

import jakarta.validation.Valid;
import nl.novi.eventmanager900102055.dtos.ArtistDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.services.ArtistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/artists")
public class ArtistController {
    private final ArtistService artistService;

    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }

    @PostMapping(value = "/create_artist")
    public ResponseEntity<Object> createArtist(@Valid @RequestBody ArtistDto ArtistDto, BindingResult bindingResult) throws NameDuplicateException, ResourceNotFoundException {

        if (bindingResult.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            ArtistDto dto = artistService.createArtist(ArtistDto);
            URI location = UriComponentsBuilder
                    .fromPath("/Artists/{lastName}")
                    .buildAndExpand(dto.getName())
                    .toUri();

            return ResponseEntity.created(location).body("Artist created: " + dto.getName() + " with id: " + +dto.getId());
        }
    }

    @GetMapping(value = "/find_all_artists")
    public ResponseEntity<List<ArtistDto>> findAllArtists() {
        List<ArtistDto> ArtistDtoList;
        ArtistDtoList = artistService.findAllArtists();
        return ResponseEntity.ok().body(ArtistDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findArtistById(@PathVariable("id") Long id) {
        try {
            ArtistDto artistDto = artistService.findArtistById(id);
            return ResponseEntity.ok().body(artistDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the artist: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping(value = "/find_artist_by_name")
    public ResponseEntity<Object> findArtistByName(@RequestBody Map<String, Object> requestBody) {
        try {
            ArtistDto EventDto = artistService.findArtistByName(requestBody.get("name").toString());
            return ResponseEntity.ok().body(EventDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the Artist: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateArtist(@PathVariable("id") Long id, @Valid @RequestBody ArtistDto ArtistDto, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("/n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            ArtistDto updatedArtistDto = artistService.updateArtist(id, ArtistDto);
            return ResponseEntity.ok().body(updatedArtistDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable("id") Long id) {
        boolean deleted = artistService.deleteArtist(id);
        if (deleted) {
            return ResponseEntity.ok("Artist deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
