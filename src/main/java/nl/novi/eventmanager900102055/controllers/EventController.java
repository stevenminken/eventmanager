package nl.novi.eventmanager900102055.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import jakarta.validation.Valid;
import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.dtos.TicketDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.services.EventService;
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
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    private final ObjectMapper objectMapper;

    public EventController(EventService eventService, ObjectMapper objectMapper) {
        this.eventService = eventService;
        this.objectMapper = objectMapper;
    }

    @PostMapping("/{username}")
    public ResponseEntity<Object> createEvent(@Valid @RequestBody EventDto EventDto, BindingResult bindingResult, @PathVariable("username") String username) throws NameDuplicateException, ResourceNotFoundException {

        if (bindingResult.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("\n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            EventDto dto = eventService.createEvent(EventDto, username);
            URI location = UriComponentsBuilder
                    .fromPath("/Events/{lastName}")
                    .buildAndExpand(dto.getName())
                    .toUri();

            return ResponseEntity.created(location).body("Event created: " + dto.getName() + " with id: " + +dto.getId());
        }
    }

    @GetMapping
    public ResponseEntity<Object> findAllEvents() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);

        List<EventDto> eventDtoList = eventService.findAllEvents();
        try {
            String json = objectMapper.writeValueAsString(eventDtoList);
            return ResponseEntity.ok().body(json);
        } catch (JsonProcessingException e) {
            String errorMessage = "Error occurred while serializing event data: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<Object> findEventById(@PathVariable("id") Long id) {
        try {
            EventDto eventDto = eventService.findEventById(id);
            return ResponseEntity.ok().body(eventDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the Event: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping
    public ResponseEntity<Object> findEventByName(@RequestBody Map<String, Object> requestBody) {
        try {
            EventDto EventDto = eventService.findEventByName(requestBody.get("name").toString());
            return ResponseEntity.ok().body(EventDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the Event: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> updateEvent(@PathVariable("id") Long id, @Valid @RequestBody EventDto EventDto, BindingResult br) {
        if (br.hasFieldErrors()) {
            StringBuilder sb = new StringBuilder();
            for (FieldError fe : br.getFieldErrors()) {
                sb.append(fe.getField()).append(": ");
                sb.append(fe.getDefaultMessage());
                sb.append("/n");
            }
            return ResponseEntity.badRequest().body(sb.toString());
        } else {
            EventDto updatedEventDto = eventService.updateEvent(id, EventDto);
            return ResponseEntity.ok().body(updatedEventDto);
        }
    }

    @PostMapping("/add_location")
    public ResponseEntity<Object> addLocationToEvent(@RequestBody Map<String, Object> requestBody) {
        try {
            Long eventId = Long.parseLong(requestBody.get("eventId").toString());
            Long locationId = Long.parseLong(requestBody.get("locationId").toString());
            boolean added = eventService.addLocationToEvent(eventId, locationId);
            if (added) {
                return ResponseEntity.ok().body("Location added");
            } else {
                return ResponseEntity.badRequest().body("Location not added");
            }
        } catch (Exception e) {
            String errorMessage = "Error occurred while adding the location: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @PostMapping("/add_artist")
    public ResponseEntity<Object> addArtistToEvent(@RequestBody Map<String, Object> requestBody) {
        try {
            Long eventId = Long.parseLong(requestBody.get("eventId").toString());
            Long artistId = Long.parseLong(requestBody.get("artistId").toString());
            boolean added = eventService.addArtistToEvent(eventId, artistId);
            if (added) {
                return ResponseEntity.ok().body("Artist added");
            } else {
                return ResponseEntity.badRequest().body("Artist not added");
            }
        } catch (Exception e) {
            String errorMessage = "Error occurred while adding the artist: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteEvent(@PathVariable("id") Long id) {
        boolean deleted = eventService.deleteEvent(id);
        if (deleted) {
            return ResponseEntity.ok().body("Event deleted");
        } else {
            return ResponseEntity.badRequest().body("Event not deleted");
        }
    }

}
