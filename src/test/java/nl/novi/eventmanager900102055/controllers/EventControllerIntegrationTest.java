package nl.novi.eventmanager900102055.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.eventmanager900102055.dtos.EventDto;
import nl.novi.eventmanager900102055.services.EventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class EventControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EventService eventService;

    final private ObjectMapper objectMapper = new ObjectMapper();
    private EventDto eventDto;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());

        eventDto = new EventDto();
        eventDto.setId(1L);
        eventDto.setName("Test Event");
        eventDto.setDate(LocalDate.of(2024, 1, 1));
        eventDto.setAvailability(2000);
        eventDto.setTicketsSold(0);
    }

    @Test
    @DisplayName("Create Event with Valid Data - Should Create Event")
    public void testCreateEvent_WithValidData_ShouldCreateEvent() throws Exception {


        when(eventService.createEvent(any(EventDto.class))).thenReturn(eventDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/events/create_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.header().string("Location", "/Events/Test%20Event"))
                .andExpect(MockMvcResultMatchers.content().string("Event created: Test Event with id: 1"));
    }

    @Test
    @DisplayName("Find All Events - Should Return List of Events")
    public void testFindAllEvents_ShouldReturnListOfEvents() throws Exception {

        EventDto event2 = new EventDto();
        event2.setId(2L);
        event2.setName("Event 2");
        event2.setDate(LocalDate.of(2023, 9, 15));
        event2.setAvailability(500);
        event2.setTicketsSold(200);

        List<EventDto> eventDtoList = Arrays.asList(eventDto, event2);

        when(eventService.findAllEvents()).thenReturn(eventDtoList);

        mockMvc.perform(MockMvcRequestBuilders.get("/events/find_all_events"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(eventDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(eventDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].date").value(eventDto.getDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].availability").value(eventDto.getAvailability()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ticketsSold").value(eventDto.getTicketsSold()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(event2.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(event2.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].date").value(event2.getDate().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].availability").value(event2.getAvailability()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].ticketsSold").value(event2.getTicketsSold()));
    }

    @Test
    @DisplayName("Find Event by ID with Valid ID - Should Return Event")
    public void testFindEventById_WithValidId_ShouldReturnEvent() throws Exception {

        when(eventService.findEventById(eq(1L))).thenReturn(eventDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/events/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Event"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2024-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availability").value(2000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketsSold").value(0));
    }


    @Test
    @DisplayName("Find Event by Name with Valid Name - Should Return Event")
    public void testFindEventByName_WithValidName_ShouldReturnEvent() throws Exception {

        when(eventService.findEventByName(eq("Test Event"))).thenReturn(eventDto);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", "Test Event");

        mockMvc.perform(MockMvcRequestBuilders.post("/events/find_event_by_name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Test Event"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2024-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availability").value(2000))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketsSold").value(0));
    }


    @Test
    @DisplayName("Update Event with Valid Data - Should Return Updated Event")
    public void testUpdateEvent_WithValidData_ShouldReturnUpdatedEvent() throws Exception {

        EventDto updatedEventDto = new EventDto();
        updatedEventDto.setId(1L);
        updatedEventDto.setName("Updated Event");
        updatedEventDto.setDate(LocalDate.of(2025, 1, 1));
        updatedEventDto.setAvailability(1500);
        updatedEventDto.setTicketsSold(500);

        when(eventService.updateEvent(eq(1L), any(EventDto.class))).thenReturn(updatedEventDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(eventDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Updated Event"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.date").value("2025-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.availability").value(1500))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ticketsSold").value(500));
    }


    @Test
    @DisplayName("Add Location to Event with Valid Data - Should Return Success Message")
    public void testAddLocationToEvent_WithValidData_ShouldReturnSuccessMessage() throws Exception {

        Long eventId = 1L;
        Long locationId = 100L;

        when(eventService.addLocationToEvent(eq(eventId), eq(locationId))).thenReturn(true);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("eventId", eventId);
        requestBody.put("locationId", locationId);

        mockMvc.perform(MockMvcRequestBuilders.post("/events/add_location")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Location added"));
    }


    @Test
    @DisplayName("Add Artist to Event with Valid Data - Should Return Success Message")
    public void testAddArtistToEvent_WithValidData_ShouldReturnSuccessMessage() throws Exception {

        Long eventId = 1L;
        Long artistId = 200L;

        when(eventService.addArtistToEvent(eq(eventId), eq(artistId))).thenReturn(true);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("eventId", eventId);
        requestBody.put("artistId", artistId);

        mockMvc.perform(MockMvcRequestBuilders.post("/events/add_artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Artist added"));
    }


    @Test
    @DisplayName("Delete Event with Valid Event ID - Should Return Success Message")
    public void testDeleteEvent_WithValidEventId_ShouldReturnSuccessMessage() throws Exception {

        Long eventId = 1L;

        when(eventService.deleteEvent(eq(eventId))).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/events/{id}", eventId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Event deleted"));
    }
}
