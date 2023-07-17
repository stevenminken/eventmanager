package nl.novi.eventmanager900102055.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Artist;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.Location;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.ArtistRepository;
import nl.novi.eventmanager900102055.repositories.EventRepository;
import nl.novi.eventmanager900102055.repositories.LocationRepository;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final ArtistRepository artistRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository, LocationRepository locationRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.locationRepository = locationRepository;
        this.artistRepository = artistRepository;
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

    public boolean addLocationToEvent(Long eventId, Long locationId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Location location = locationRepository.findById(locationId).orElse(null);
        if (event == null || location == null || event.getLocation() != null) {
            return false;
        }
        event.setLocation(location);
        eventRepository.save(event);
        return true;
    }

    public boolean addArtistToEvent(Long eventId, Long artistId) {
        Event event = eventRepository.findById(eventId).orElse(null);
        Artist artist = artistRepository.findById(artistId).orElse(null);
        if (event == null || artist == null) {
            return false;
        }
        event.getArtistList().add(artist);
        eventRepository.save(event);
        return true;
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
        event.setLocation((eventDto.getLocation()));
        event.setArtistList(eventDto.getArtistList());
        event.setTicketList(eventDto.getTicketList());

        return event;
    }

    public EventDto transferEventToEventDto(Event event) {
        EventDto eventDto = new EventDto();

        eventDto.setId(event.getId());
        eventDto.setName(event.getName());
        eventDto.setDate(event.getDate());
        eventDto.setAvailability(event.getAvailability());
        eventDto.setTicketsSold(event.getTicketsSold());
        eventDto.setLocation((event.getLocation()));
        eventDto.setArtistList(event.getArtistList());
        eventDto.setTicketList(event.getTicketList());

        return eventDto;
    }

}
