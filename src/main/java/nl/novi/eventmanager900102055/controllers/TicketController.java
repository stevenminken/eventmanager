package nl.novi.eventmanager900102055.controllers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.dtos.TicketDto;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.exceptions.TicketsSoldOutException;
import nl.novi.eventmanager900102055.services.TicketService;
import org.hibernate.Hibernate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tickets")
public class TicketController {
    private final TicketService ticketService;
    private final ObjectMapper objectMapper;

    public TicketController(TicketService ticketService, ObjectMapper objectMapper) {
        this.ticketService = ticketService;
        this.objectMapper = objectMapper;
    }

    @PostMapping(value = "/create_ticket")
    public ResponseEntity<Object> createTicket(@RequestBody Map<String, Object> requestBody) throws ResourceNotFoundException, TicketsSoldOutException {
        long eventId = Long.parseLong(requestBody.get("eventId").toString());
        String username = requestBody.get("username").toString();
        double price = Double.parseDouble(requestBody.get("price").toString());

        if (eventId <= 0 || username == null || username.length() == 0 || price <= 0) {
            return ResponseEntity.badRequest().body("Invalid request parameters");
        }

        TicketDto dto = ticketService.createTicket(eventId, username, price);
        URI location = UriComponentsBuilder
                .fromPath("/Tickets/{id}")
                .buildAndExpand(dto.getId())
                .toUri();

        return ResponseEntity.created(location).body("Ticket created: " + dto.getEvent().getName() + " with id: " + dto.getId());
    }

    @GetMapping(value = "/find_all_tickets")
    public ResponseEntity<Object> findAllTickets() {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, true);

        List<TicketDto> ticketDtoList = ticketService.findAllTickets();
        try {
            String json = objectMapper.writeValueAsString(ticketDtoList);
            return ResponseEntity.ok().body(json);
        } catch (JsonProcessingException e) {
            String errorMessage = "Error occurred while serializing ticket data: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @PostMapping(value = "/tickets_user")
    public ResponseEntity<Object> findUserTickets(@RequestBody Map<String, Object>  requestBody) {
        try {
            List<TicketDto> ticketDtoList = ticketService.findUserTickets(requestBody.get("userId").toString());
            return ResponseEntity.ok().body(ticketDtoList);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the tickets: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findTicketById(@PathVariable("id") Long id) {
        try {
            TicketDto TicketDto = ticketService.findTicketById(id);
            return ResponseEntity.ok().body(TicketDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the ticket: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTicket(@PathVariable("id") Long id) {
        boolean deleted = ticketService.deleteTicket(id);
        if (deleted) {
            return ResponseEntity.ok("Ticket deleted");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}

