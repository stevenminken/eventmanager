package nl.novi.eventmanager900102055.services;

import jakarta.transaction.Transactional;
import nl.novi.eventmanager900102055.dtos.TicketDto;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.exceptions.TicketsSoldOutException;
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
import java.util.*;
import java.util.stream.Collectors;

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

    public TicketDto createTicket(Long eventId, String username, Double price) throws ResourceNotFoundException, TicketsSoldOutException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new ResourceNotFoundException("User does not exist");
        } else {

            Event event = eventRepository.findById(eventId)
                    .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

            if (event.getAvailability() - event.getTicketsSold() > 0) {
                Ticket ticket = new Ticket(user, event, price);
                event.setAvailability(event.getAvailability() - 1);
                event.setTicketsSold(event.getTicketsSold() + 1);

                Transaction transaction = new Transaction();
                transaction.setDateOfPurchase(LocalDate.now());
                transaction.setPaymentMethod("creditcard");
                transaction.setTicket(ticket);
                ticket.setTransaction(transaction);

                userRepository.save(user);
                eventRepository.save(event);

                return transferTicketToTicketDto(ticketRepository.save(ticket));

            } else {
                throw new TicketsSoldOutException("Tickets sold out");
            }
        }
    }

    @Transactional
    public List<TicketDto> findAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream().map(this::transferTicketToTicketDto).collect(Collectors.toList());
    }

    public List<TicketDto> findUserTickets(String userId) {
        ArrayList<Ticket> ticketList = new ArrayList<>(ticketRepository.findAll());
        ArrayList<Ticket> userTicketList = new ArrayList<>();
        for (Ticket t : ticketList) {
            if (t.getUser().getUsername().equals(userId)) {
                userTicketList.add(t);
            }
        }
        return transferTicketListToTicketDtoList(userTicketList);
    }

    public TicketDto findTicketById(Long id) throws ResourceNotFoundException {
        Ticket ticket = ticketRepository.findTicketByIdWithEagerFetch(id);
        if (ticket == null) {
            throw new ResourceNotFoundException("Ticket not found with id: " + id);
        }
        return transferTicketToTicketDto(ticket);
    }

    public boolean deleteTicket(Long id) {
        if (ticketRepository.existsById(id)) {
            Ticket ticket = ticketRepository.findById(id).orElse(null);
            if (ticket == null) {
                return false;
            }

            if (transactionRepository.existsById(ticket.getTransaction().getId())) {
                transactionRepository.deleteById(ticket.getTransaction().getId());
            }

            Event event = ticket.getEvent();
            ArrayList<Ticket> ticketListEvent = new ArrayList<>(event.getTicketList());
            Iterator<Ticket> eventIterator = ticketListEvent.iterator();
            while (eventIterator.hasNext()) {
                Ticket ticketEvent = eventIterator.next();
                if (Objects.equals(ticketEvent.getId(), ticket.getId())) {
                    eventIterator.remove();
                    break;
                }
            }
            event.setAvailability(event.getAvailability() + 1);
            event.setTicketsSold(event.getTicketsSold() - 1);

            User user = ticket.getUser();
            ArrayList<Ticket> ticketListUser = new ArrayList<>(user.getTicketList());
            Iterator<Ticket> userIterator = ticketListUser.iterator();
            while (userIterator.hasNext()) {
                Ticket ticketUser = userIterator.next();
                if (Objects.equals(ticketUser.getId(), ticket.getId())) {
                    userIterator.remove();
                    break;
                }
            }

            userRepository.save(user);
            eventRepository.save(event);
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

        ticketDto.setId(ticket.getId());
        ticketDto.setPrice(ticket.getPrice());

        ticketDto.setEvent(ticket.getEvent());
        ticketDto.setTransaction(ticket.getTransaction());
        ticketDto.setUser(ticket.getUser());

        return ticketDto;
    }
}

