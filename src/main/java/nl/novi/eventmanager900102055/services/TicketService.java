package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.TicketDto;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.Ticket;
import nl.novi.eventmanager900102055.models.Transaction;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.EventRepository;
import nl.novi.eventmanager900102055.repositories.TicketRepository;
import nl.novi.eventmanager900102055.repositories.TransactionRepository;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TransactionRepository transactionRepository;
    private final EventService eventService;
    private final UserService userService;
    private final TransactionService transactionService;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, EventRepository eventRepository, TransactionRepository transactionRepository, EventService eventService, UserService userService, TransactionService transactionService) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.transactionRepository = transactionRepository;
        this.eventService = eventService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    public TicketDto createTicket(Long eventId, String username, Double price) throws ResourceNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        } else {

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            Ticket ticket = new Ticket(user, event, price);

            Transaction transaction = new Transaction();
            transaction.setDateOfPurchase(LocalDate.now());
            transaction.setPaymentMethod("creditcard");
            transaction.setTicket(ticket);

            ticket.setTransaction(transaction);

            user.getTicketList().add(ticket);
            event.getTicketList().add(ticket);

            userRepository.save(user);
            eventRepository.save(event);

            return transferTicketToTicketDto(ticketRepository.save(ticket));
        }
    }

    public List<TicketDto> findAllTickets() {
        List<Ticket> ticketList = ticketRepository.findAll();
        return transferTicketListToTicketDtoList(ticketList);
    }

    public TicketDto findTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket == null) {
            return null;
        }
        return transferTicketToTicketDto(ticket);
    }

    public boolean deleteTicket(Long id) {
        if (ticketRepository.existsById(id)) {
            Ticket ticket = ticketRepository.findById(id).orElse(null);
            if (ticket != null && transactionRepository.existsById(ticket.getTransaction().getId())) {
                transactionRepository.findById(ticket.getTransaction().getId()).ifPresent(transaction -> transactionRepository.deleteById(transaction.getId()));
            }
            ticketRepository.deleteById(id);

            return true;
        }
        return false;
    }

    public List<TicketDto> transferTicketListToTicketDtoList(List<Ticket> ticketList) {
        List<TicketDto> ticketDtoList = new ArrayList<>();

        for (Ticket ticket : ticketList) {
            ticketDtoList.add(transferTicketToTicketDto(ticket));

        }
        return ticketDtoList;
    }

//    public Ticket transferTicketDtoToTicket(TicketDto ticketDto) {
//        Ticket ticket = new Ticket();
//        ticket.setId(ticketDto.getId());
//        ticket.setPrice(ticketDto.getPrice());
//        ticket.setEvent(ticketDto.getEvent());
//        ticket.setTransaction(ticketDto.getTransaction());
//        ticket.setUser(ticketDto.getUser());
//
//        return ticket;
//    }

    public TicketDto transferTicketToTicketDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();
        System.out.println(ticket.getId());

        ticketDto.setId(ticket.getId());
        ticketDto.setPrice(ticket.getPrice());

        ticketDto.setEventDto(eventService.transferEventToEventDto(ticket.getEvent()));
        ticketDto.setTransactionDto(transactionService.transferTransactionToTransactionDto(ticket.getTransaction()));
        ticketDto.setUserDto(userService.transferUserToUserDto(ticket.getUser()));

        return ticketDto;
    }
}
