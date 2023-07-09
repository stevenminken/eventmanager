package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.TicketDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.Ticket;
import nl.novi.eventmanager900102055.models.Transaction;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.EventRepository;
import nl.novi.eventmanager900102055.repositories.TicketRepository;
import nl.novi.eventmanager900102055.repositories.TransactionRepository;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final TransactionRepository transactionRepository;

    public TicketService(TicketRepository ticketRepository, UserRepository userRepository, EventRepository eventRepository, TransactionRepository transactionRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<TicketDto> findAllTickets() {
        List<Ticket> ticketList = ticketRepository.findAll();
        return transferTicketListToTicketDtoList(ticketList);
    }

    public TicketDto buyTicket(long eventId, long userId, Double price) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event not found"));

        Ticket ticket = new Ticket(user, event, price);

        Transaction transaction = new Transaction();
        transaction.setTicket(ticket);
        ticket.setTransaction(transaction);

        user.getTicketList().add(ticket);
        event.getTicketList().add(ticket);

        userRepository.save(user);
        eventRepository.save(event);
        transactionRepository.save(transaction);

        return transferTicketToTicketDto(ticketRepository.save(ticket));
    }

    public TicketDto findTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket == null) {
            return null;
        }
        return transferTicketToTicketDto(ticket);
    }

    public TicketDto updateTicket(Long id, TicketDto ticketDto) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket == null) {
            return null;
        }
        ticket.setId(ticketDto.getId());
        ticket.setPrice(ticketDto.getPrice());
        return transferTicketToTicketDto(ticketRepository.save(ticket));
    }

    public boolean deleteTicket(Long id) {
        if (ticketRepository.existsById(id)) {
            ticketRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<TicketDto> transferTicketListToTicketDtoList(List<Ticket> ticketList) {
        List<TicketDto> ticketDtoList = new ArrayList<>();

        for (Ticket ticket : ticketList) {
            TicketDto ticketDto = new TicketDto();
            ticketDto.setId(ticket.getId());
            ticketDto.setPrice(ticket.getPrice());
            ticketDtoList.add(ticketDto);
        }
        return ticketDtoList;
    }

    public Ticket transferTicketDtoToTicket(TicketDto ticketDto) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDto.getId());
        ticket.setPrice(ticketDto.getPrice());

        return ticket;
    }

    public TicketDto transferTicketToTicketDto(Ticket ticket) {
        TicketDto ticketDto = new TicketDto();

        ticketDto.setId(ticket.getId());
        ticketDto.setPrice(ticket.getPrice());

        return ticketDto;
    }
}
