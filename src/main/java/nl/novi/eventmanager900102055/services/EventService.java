package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.ArtistDto;
import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.ArtistRepository;
import nl.novi.eventmanager900102055.repositories.EventRepository;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
    }

    public EventDto createEvent(EventDto eventDto, String username) throws NameDuplicateException, ResourceNotFoundException {
        User user = userRepository.findById(username).orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        Iterable<Event> events = eventRepository.findAll();
        for (Event event : events) {
            if (event.getName().equals(eventDto.getName())) {
                throw new NameDuplicateException("This event already exists");
            }
        }
        Event event = transferEventDtoToEvent(eventDto);

        event.setUser(user);
        user.getEventList().add(event);

        userRepository.save(user);
        event = eventRepository.save(event);
        return transferEventToEventDto(event);
    }

    public List<EventDto> findAllEvents() {
        List<Event> eventList = eventRepository.findAll();
        return transferEventListToEventDtoList(eventList);
    }

    public EventDto findEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("event not found"));
        return transferEventToEventDto(event);
    }

    public EventDto findEventByName(String name) throws ResourceNotFoundException {
        Event event = eventRepository.findByName(name);
        if (event == null) {
            throw new ResourceNotFoundException("Event not found");
        }
        return transferEventToEventDto(event);
    }

    public EventDto updateEvent(Long id, EventDto eventDto) {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            return null;
        }
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setAvailability(eventDto.getAvailability());
        event.setTicketsSold(eventDto.getTicketsSold());

        return transferEventToEventDto(eventRepository.save(event));
    }

    public boolean deleteEvent(Long id) {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<EventDto> transferEventListToEventDtoList(List<Event> eventList) {
        List<EventDto> eventDtoList = new ArrayList<>();

        for (Event event : eventList) {
            eventDtoList.add(transferEventToEventDto(event));
        }
        return eventDtoList;
    }

    public Event transferEventDtoToEvent(EventDto eventDto) {
        Event event = new Event();
        event.setId(eventDto.getId());
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setAvailability(eventDto.getAvailability());
        event.setTicketsSold(eventDto.getTicketsSold());

        return event;
    }

    public EventDto transferEventToEventDto(Event event) {
        EventDto eventDto = new EventDto();

        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setDate(event.getDate());
        eventDto.setAvailability(event.getAvailability());
        eventDto.setTicketsSold(event.getTicketsSold());

        return eventDto;
    }

}
