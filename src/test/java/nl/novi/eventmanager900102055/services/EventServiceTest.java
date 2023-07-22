package nl.novi.eventmanager900102055.services;

import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.exceptions.NameDuplicateException;
import nl.novi.eventmanager900102055.exceptions.ResourceNotFoundException;
import nl.novi.eventmanager900102055.models.Artist;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.Location;
import nl.novi.eventmanager900102055.repositories.ArtistRepository;
import nl.novi.eventmanager900102055.repositories.EventRepository;
import nl.novi.eventmanager900102055.repositories.LocationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @Mock
    private LocationRepository locationRepository;
    @Mock
    private ArtistRepository artistRepository;
    @InjectMocks
    private EventService eventService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("Create Event with Valid Data - Should Create Event")
    void testCreateEvent_WithValidData_ShouldCreateEvent() throws NameDuplicateException {
        // Arrange
        EventDto eventDto = new EventDto();
        eventDto.setName("Test Event");
        eventDto.setDate(LocalDate.now());
        eventDto.setAvailability(100);
        eventDto.setTicketsSold(0);

        Event savedEvent = new Event();
        savedEvent.setId(1L);
        savedEvent.setName("Test Event");
        savedEvent.setDate(LocalDate.now());
        savedEvent.setAvailability(100);
        savedEvent.setTicketsSold(0);

        when(eventRepository.save(any(Event.class))).thenReturn(savedEvent);
        when(eventRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        EventDto createdEventDto = eventService.createEvent(eventDto);

        // Assert
        assertNotNull(createdEventDto);
        assertEquals("Test Event", createdEventDto.getName());
        assertEquals(LocalDate.now(), createdEventDto.getDate());
        assertEquals(100, createdEventDto.getAvailability());
        assertEquals(0, createdEventDto.getTicketsSold());
    }

    @Test
    @DisplayName("Create Event with Duplicate Name - Should Throw NameDuplicateException")
    void testCreateEvent_WithDuplicateName_ShouldThrowNameDuplicateException() {

        EventDto eventDto = new EventDto();
        eventDto.setName("Test Event");
        eventDto.setDate(LocalDate.now());
        eventDto.setAvailability(100);
        eventDto.setTicketsSold(0);

        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setName("Test Event");

        List<Event> events = new ArrayList<>();
        events.add(existingEvent);

        when(eventRepository.findAll()).thenReturn(events);

        assertThrows(NameDuplicateException.class, () -> eventService.createEvent(eventDto));
    }

    @Test
    @DisplayName("Find All Events - Should Return List of Events")
    void testFindAllEvents_ShouldReturnListOfEvents() {
        // Arrange
        List<Event> mockEventList = new ArrayList<>();
        mockEventList.add(new Event());
        mockEventList.add(new Event());

        when(eventRepository.findAll()).thenReturn(mockEventList);

        List<EventDto> result = eventService.findAllEvents();

        assertEquals(2, result.size());
    }

    @Test
    @DisplayName("Find Event By Id - With Valid Id, Should Return EventDto")
    void testFindEventById_WithValidId_ShouldReturnEventDto() {

        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setName("Test Event");
        mockEvent.setDate(LocalDate.of(2024, 1, 3));
        mockEvent.setAvailability(1000);
        mockEvent.setTicketsSold(0);

        when(eventRepository.findById(anyLong())).thenReturn(Optional.of(mockEvent));

        EventDto result = eventService.findEventById(12345L);

        assertNotNull(result);
        assertEquals("Test Event", result.getName());
    }

    @Test
    @DisplayName("Find Event By Id - With Invalid Id, Should Throw IllegalArgumentException")
    void testFindEventById_WithInvalidId_ShouldThrowIllegalArgumentException() {

        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> eventService.findEventById(1L));
    }

    @Test
    @DisplayName("Find Event By Name - With Valid Name, Should Return EventDto")
    void testFindEventByName_WithValidName_ShouldReturnEventDto() throws ResourceNotFoundException {

        Event mockEvent = new Event();
        mockEvent.setId(1L);
        mockEvent.setName("Test Event");

        when(eventRepository.findByName("Test Event")).thenReturn(mockEvent);

        EventDto result = eventService.findEventByName("Test Event");

        assertNotNull(result);
        assertEquals("Test Event", result.getName());
    }

    @Test
    @DisplayName("Find Event By Name - With Invalid Name, Should Throw ResourceNotFoundException")
    void testFindEventByName_WithInvalidName_ShouldThrowResourceNotFoundException() {

        when(eventRepository.findByName("Non-existing Event")).thenReturn(null);

        assertThrows(ResourceNotFoundException.class, () -> eventService.findEventByName("Non-existing Event"));
    }
    @Test
    @DisplayName("Update Event - With Valid ID, Should Update Event")
    void testUpdateEvent_WithValidId_ShouldUpdateEvent() {

        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setName("Updated Event");
        eventDto.setDate(LocalDate.now());
        eventDto.setAvailability(200);
        eventDto.setTicketsSold(50);

        Event existingEvent = new Event();
        existingEvent.setId(1L);
        existingEvent.setName("Original Event");
        existingEvent.setDate(LocalDate.of(2023, 1, 1));
        existingEvent.setAvailability(100);
        existingEvent.setTicketsSold(25);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(existingEvent));
        when(eventRepository.save(any(Event.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EventDto updatedEventDto = eventService.updateEvent(1L, eventDto);

        assertNotNull(updatedEventDto);
        assertEquals("Updated Event", updatedEventDto.getName());
        assertEquals(LocalDate.now(), updatedEventDto.getDate());
        assertEquals(200, updatedEventDto.getAvailability());
        assertEquals(50, updatedEventDto.getTicketsSold());
    }

    @Test
    @DisplayName("Update Event - With Invalid ID, Should Return Null")
    void testUpdateEvent_WithInvalidId_ShouldReturnNull() {

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        EventDto updatedEventDto = eventService.updateEvent(1L, new EventDto());

        assertNull(updatedEventDto);
    }

    @Test
    @DisplayName("Add Location to Event - With Valid Data, Should Add Location to Event")
    void testAddLocationToEvent_WithValidData_ShouldAddLocationToEvent() {

        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");

        Location location = new Location();
        location.setId(1L);
        location.setName("Test Location");
        location.setEventList(new ArrayList<>());

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        boolean added = eventService.addLocationToEvent(1L, 1L);

        assertTrue(added);
        assertEquals(location, event.getLocation());
        assertTrue(location.getEventList().contains(event));
    }

    @Test
    @DisplayName("Add Location to Event - With Invalid Data, Should Return False")
    void testAddLocationToEvent_WithInvalidData_ShouldReturnFalse() {

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());
        when(locationRepository.findById(1L)).thenReturn(Optional.empty());

        boolean added = eventService.addLocationToEvent(1L, 1L);

        assertFalse(added);
    }

    @Test
    @DisplayName("Add Location to Event - With Event Already Having Location, Should Return False")
    void testAddLocationToEvent_WithEventAlreadyHavingLocation_ShouldReturnFalse() {

        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");

        Location location = new Location();
        location.setId(1L);
        location.setName("Test Location");

        event.setLocation(location);

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(locationRepository.findById(1L)).thenReturn(Optional.of(location));

        boolean added = eventService.addLocationToEvent(1L, 1L);

        assertFalse(added);
    }

    @Test
    @DisplayName("Add Artist to Event - With Valid Data, Should Add Artist to Event")
    void testAddArtistToEvent_WithValidData_ShouldAddArtistToEvent() {

        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");

        Artist artist = new Artist();
        artist.setId(1L);
        artist.setName("Test Artist");

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(artistRepository.findById(1L)).thenReturn(Optional.of(artist));

        boolean added = eventService.addArtistToEvent(1L, 1L);

        assertTrue(added);
        assertTrue(event.getArtistList().contains(artist));
        assertTrue(artist.getEventList().contains(event));
    }

    @Test
    @DisplayName("Add Artist to Event - With Invalid Data, Should Return False")
    void testAddArtistToEvent_WithInvalidData_ShouldReturnFalse() {

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());
        when(artistRepository.findById(1L)).thenReturn(Optional.empty());

        boolean added = eventService.addArtistToEvent(1L, 1L);

        assertFalse(added);
    }
    @Test
    @DisplayName("Delete Event - With Valid Id, Should Delete Event and Remove from Artists")
    void testDeleteEvent_WithValidId_ShouldDeleteEventAndRemoveFromArtists() {

        Long eventId = 1L;
        Event event = new Event();
        event.setId(eventId);
        event.setName("Test Event");
        event.setArtistList(new ArrayList<>());

        Artist artist1 = new Artist();
        artist1.setId(101L);
        artist1.setName("Artist 1");

        Artist artist2 = new Artist();
        artist2.setId(102L);
        artist2.setName("Artist 2");

        List<Artist> artists = new ArrayList<>();
        artists.add(artist1);
        artists.add(artist2);

        event.setArtistList(artists);

        when(eventRepository.existsById(eventId)).thenReturn(true);
        when(eventRepository.findById(eventId)).thenReturn(Optional.of(event));

        boolean deleted = eventService.deleteEvent(eventId);

        assertTrue(deleted);
        verify(eventRepository, times(1)).deleteById(eventId);

        ArgumentCaptor<Artist> artistCaptor = ArgumentCaptor.forClass(Artist.class);
        verify(artistRepository, times(2)).save(artistCaptor.capture());

        List<Artist> capturedArtists = artistCaptor.getAllValues();
        assertEquals(2, capturedArtists.size());

        for (Artist capturedArtist : capturedArtists) {
            assertFalse(capturedArtist.getEventList().contains(event));
        }
    }

    @Test
    @DisplayName("Delete Event - With Invalid Id, Should Return False")
    void testDeleteEvent_WithInvalidId_ShouldReturnFalse() {

        when(eventRepository.existsById(1L)).thenReturn(false);

        boolean deleted = eventService.deleteEvent(1L);

        assertFalse(deleted);
    }
    @Test
    @DisplayName("Transfer EventDto to Event - Should Convert Dto to Event")
    void testTransferEventDtoToEvent_ShouldConvertDtoToEvent() {

        EventDto eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setName("Test Event");
        eventDto.setDate(LocalDate.of(2023, 1, 1));
        eventDto.setAvailability(100);
        eventDto.setTicketsSold(50);

        Event event = eventService.transferEventDtoToEvent(eventDto);

        assertNotNull(event);
        assertEquals(1L, event.getId());
        assertEquals("Test Event", event.getName());
        assertEquals(LocalDate.of(2023, 1, 1), event.getDate());
        assertEquals(100, event.getAvailability());
        assertEquals(50, event.getTicketsSold());
    }

    @Test
    @DisplayName("Transfer Event to EventDto - Should Convert Event to Dto")
    void testTransferEventToEventDto_ShouldConvertEventToDto() {

        Event event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setDate(LocalDate.of(2023, 1, 1));
        event.setAvailability(100);
        event.setTicketsSold(50);

        EventDto eventDto = eventService.transferEventToEventDto(event);

        assertNotNull(eventDto);
        assertEquals(1L, eventDto.getId());
        assertEquals("Test Event", eventDto.getName());
        assertEquals(LocalDate.of(2023, 1, 1), eventDto.getDate());
        assertEquals(100, eventDto.getAvailability());
        assertEquals(50, eventDto.getTicketsSold());
    }
}