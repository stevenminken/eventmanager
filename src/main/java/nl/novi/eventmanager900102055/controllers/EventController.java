package nl.novi.eventmanager900102055.controllers;

import jakarta.validation.Valid;
import nl.novi.eventmanager900102055.dtos.EventDto;
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

    public EventController(EventService eventService) {

        this.eventService = eventService;
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
    public ResponseEntity<List<EventDto>> findAllEvents() {
        List<EventDto> eventDtoList;
        eventDtoList = eventService.findAllEvents();
        return ResponseEntity.ok().body(eventDtoList);
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
