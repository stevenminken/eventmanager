package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.repositories.*;
import org.springframework.stereotype.Service;

@Service
public class appService {
    private final ArtistRepository artistRepository;
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final TicketRepository ticketRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public appService(ArtistRepository artistRepository, EventRepository eventRepository, LocationRepository locationRepository, TicketRepository ticketRepository, TransactionRepository transactionRepository, UserRepository userRepository) {
        this.artistRepository = artistRepository;
        this.eventRepository = eventRepository;
        this.locationRepository = locationRepository;
        this.ticketRepository = ticketRepository;
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

}