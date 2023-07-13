package nl.novi.eventmanager900102055.controllers;

import jakarta.validation.Valid;
import nl.novi.eventmanager900102055.dtos.ArtistDto;
import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.services.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/{username}")
    public ResponseEntity<Object> createArtist(@Valid @RequestBody ArtistDto ArtistDto, BindingResult bindingResult, @PathVariable("username") String username) throws NameDuplicateException, ResourceNotFoundException {

        if (bindingResult.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            ArtistDto dto = artistService.createArtist(ArtistDto, username);
            URI location = UriComponentsBuilder
                    .fromPath("/Artists/{lastName}")
                    .buildAndExpand(dto.getName())
                    .toUri();

            return ResponseEntity.created(location).body("Artist created: " + dto.getName() + " with id: " + +dto.getId());
        }
    }

    @GetMapping
    public ResponseEntity<List<ArtistDto>> findAllArtists() {
        List<ArtistDto> ArtistDtoList;
        ArtistDtoList = artistService.findAllArtists();
        return ResponseEntity.ok().body(ArtistDtoList);
    }

    //    http://localhost:8080/artists?id=1
    @GetMapping(params = "id")
    public ResponseEntity<Object> findArtistById(@RequestParam("id") Long id) {
        try {
            ArtistDto ArtistDto = artistService.findArtistById(id);
            return ResponseEntity.ok().body(ArtistDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the artist: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping
    public ResponseEntity<Object> findArtistByName(@RequestBody Map<String, Object> requestBody) {
        try {
            ArtistDto EventDto = artistService.findArtistByName(requestBody.get("name").toString());
            return ResponseEntity.ok().body(EventDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the Artist: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
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
            ArtistDto updatedArtistDto = artistService.updateArtist(id, ArtistDto);
            return ResponseEntity.ok().body(updatedArtistDto);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteArtist(@PathVariable("id") Long id) {
        boolean deleted = artistService.deleteArtist(id);
        if (deleted) {
            return ResponseEntity.ok().body("Artist deleted");
        } else {
            return ResponseEntity.badRequest().body("Artist not deleted");
        }
    }

}
