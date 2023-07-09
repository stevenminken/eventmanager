package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Artist;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.User;
import nl.novi.eventmanager900102055.repositories.ArtistRepository;
import nl.novi.eventmanager900102055.repositories.EventRepository;
import nl.novi.eventmanager900102055.repositories.TicketRepository;
import nl.novi.eventmanager900102055.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;

    public EventService(EventRepository eventRepository, UserRepository userRepository, ArtistRepository artistRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
    }

    public List<EventDto> findAllEvents() {
        List<Event> eventList = eventRepository.findAll();
        return transferEventListToEventDtoList(eventList);
    }

    public EventDto findEventById(Long id) throws ResourceNotFoundException {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new ResourceNotFoundException("Can't find this event");
        }
        return transferEventToEventDto(event);
    }
    public List<EventDto> getEventByName(String name) throws ResourceNotFoundException {
        Iterable<Event> iterableEvents = eventRepository.findByName(name);
        if(iterableEvents == null) {
            throw new ResourceNotFoundException("Can't find this event");
        }
        ArrayList<Event> eventList = new ArrayList<>();

        for (Event event : iterableEvents) {
            eventList.add(event);
        }

        return transferEventListToEventDtoList(eventList);
    }
    public EventDto createEvent(EventDto eventDto){return new EventDto();};

                                public EventDto createEvent(EventDto eventDto, long userId, List<Long> artistIds) throws NameDuplicateException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Iterable<Event> Events = eventRepository.findAll();
        for (Event l : Events) {
            if (l.getName().equals(eventDto.getName())) {
                throw new NameDuplicateException("This Event already exists");
            }
        }
        Event event = transferEventDtoToEvent(eventDto);

        event.setUser(user);
        user.getEventList().add(event);

        List<Artist> artists = artistRepository.findAllById(artistIds);
        event.setArtists(artists);
        for (Artist artist : artists) {
            artist.getEvents().add(event);
        }

        userRepository.save(user);

        return transferEventToEventDto(eventRepository.save(event));
    }

    public EventDto updateEvent(Long id, EventDto eventDto) throws ResourceNotFoundException {
        Event event = eventRepository.findById(id).orElse(null);
        if (event == null) {
            throw new ResourceNotFoundException("Can't find this event");
        }
        event.setName(eventDto.getName());
        event.setDate(eventDto.getDate());
        event.setAvailability(eventDto.getAvailability());
        event.setTicketsSold(eventDto.getTicketsSold());
        return transferEventToEventDto(eventRepository.save(event));
    }

    public void deleteEvent(Long id) throws ResourceNotFoundException {
        if (eventRepository.existsById(id)) {
            eventRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException("Event not found with ID: " + id);
        }
    }

//    public List<EventDto> findEventsByFilter(String filter) {
//        return eventRepository.findByFilter(filter).stream().map(Event::toDTO).collect(Collectors.toList());
//    }

    public static List<EventDto> transferEventListToEventDtoList(List<Event> eventList) {
        List<EventDto> eventDtoList = new ArrayList<>();

        for (Event event : eventList) {
            EventDto eventDto = new EventDto();
            eventDto.setId(event.getId());
            eventDto.setName(event.getName());
            eventDto.setDate(event.getDate());
            eventDto.setAvailability(event.getAvailability());
            eventDto.setTicketsSold(event.getTicketsSold());
            eventDtoList.add(eventDto);
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
