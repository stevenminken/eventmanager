package nl.novi.eventmanager900102055.controllers;

import nl.novi.eventmanager900102055.services.TicketService;

public class TicketController {
    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }
}
