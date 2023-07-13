package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.dtos.TicketDto;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
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

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping
    public ResponseEntity<Object> createTicket (@RequestBody Map<String, Object> requestBody) throws ResourceNotFoundException {
        long eventId = Long.parseLong(requestBody.get("eventId").toString());
        String username = requestBody.get("username").toString();
        double price = Double.parseDouble(requestBody.get("price").toString());

        if (eventId <= 0 || username == null || username.length() == 0 || price <= 0) {
            return ResponseEntity.badRequest().body("Invalid request parameters");
        }

//        if (bindingResult.hasFieldErrors()) {
//            StringBuilder sb = new StringBuilder();
//            for (FieldError fe : bindingResult.getFieldErrors()) {
//                sb.append(fe.getField()).append(": ");
//                sb.append(fe.getDefaultMessage());
//                sb.append("\n");
//            }
//            return ResponseEntity.badRequest().body(sb.toString());
//        } else {
            TicketDto dto = ticketService.createTicket(eventId, username, price);
            URI location = UriComponentsBuilder
                    .fromPath("/Tickets/")
                    .buildAndExpand(dto.getId())
                    .toUri();

            return ResponseEntity.created(location).body("Ticket created: with id: " + + dto.getId());
//        }
    }
    @GetMapping
    public ResponseEntity<Object> findAllTickets() {
        try {
            List<TicketDto> TicketDtoList = ticketService.findAllTickets();
            return ResponseEntity.ok().body(TicketDtoList);
        }
        catch (Exception e) {
            return ResponseEntity.ok().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> findTicketById(@PathVariable("id")  Long id) {
        try {
            TicketDto TicketDto = ticketService.findTicketById(id);
            return ResponseEntity.ok().body(TicketDto);
        } catch (Exception e) {
            String errorMessage = "Error occurred while fetching the ticket: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteTicket(@PathVariable("id") Long id) {
        boolean deleted = ticketService.deleteTicket(id);
        if (deleted) {
            return ResponseEntity.ok().body("Ticket deleted");
        } else {
            return ResponseEntity.badRequest().body("Ticket not deleted");
        }
    }
}

