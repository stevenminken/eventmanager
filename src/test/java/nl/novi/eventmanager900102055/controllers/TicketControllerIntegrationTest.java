package nl.novi.eventmanager900102055.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import nl.novi.eventmanager900102055.dtos.TicketDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = "spring.config.location=classpath:application-test.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
@Sql(scripts = "classpath:data-test.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class TicketControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();
    Map<String, Object> newEvent;
    Map<String, Object> newUser;
    Long eventId;

    @BeforeEach
    public void setUp() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //create event
        newEvent = new HashMap<>();
        newEvent.put("name", "Winterfestival");
        newEvent.put("date", "2023-12-15");
        newEvent.put("availability", 1000);
        newEvent.put("ticketsSold", 0);

        MvcResult postResult = mockMvc.perform(MockMvcRequestBuilders.post("/events/create_event")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newEvent)))
                .andReturn();

        String responseJson = postResult.getResponse().getContentAsString();
        String idString = responseJson.replaceAll("[^\\d]", "");
        eventId = Long.parseLong(idString);

        // create user
        newUser = new HashMap<>();
        newUser.put("username", "Melinda test");
        newUser.put("password", "random_password");
        newUser.put("enabled", true);
        newUser.put("email", "info@email.com");
        newUser.put("apikey", "random_api");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/create_user")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newUser)));
        // create ticket
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("eventId", eventId);
        requestBody.put("username", newUser.get("username"));
        requestBody.put("price", 50.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/tickets/create_ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Create Ticket with Valid Data - Should Create Ticket")
    public void testCreateTicket_WithValidData_ShouldCreateTicket() throws Exception {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("eventId", eventId);
        requestBody.put("username", newUser.get("username"));
        requestBody.put("price", 70.0);

        MvcResult postResultTicket = mockMvc.perform(MockMvcRequestBuilders.post("/tickets/create_ticket")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andReturn();

        String responseJsonTicket = postResultTicket.getResponse().getContentAsString();
        String idStringTicket = responseJsonTicket.replaceAll("[^\\d]", "");
        Long ticketId = Long.parseLong(idStringTicket);

        MockHttpServletResponse ticketResponse = postResultTicket.getResponse();

        assertEquals(201, ticketResponse.getStatus());
        assertEquals("/Tickets/" + ticketId, ticketResponse.getHeader("Location"));
        assertEquals("Ticket created: Winterfestival with id: " + ticketId, ticketResponse.getContentAsString());
    }
    @Test
    @DisplayName("Get All Tickets - Should Return All Tickets")
    public void testGetAllTickets_ShouldReturnAllTickets() throws Exception {
        // create extra ticket
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("eventId", eventId);
        requestBody.put("username", newUser.get("username"));
        requestBody.put("price", 50.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/tickets/create_ticket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody)));

        // Perform request
        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders.get("/tickets/find_all_tickets"))
                .andReturn();

        // Get the response content as a String
        String responseJson = getResult.getResponse().getContentAsString();

        // Deserialize the JSON response using a CollectionType
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType listType = typeFactory.constructCollectionType(List.class, TicketDto.class);
        List<TicketDto> ticketList = objectMapper.readValue(responseJson, listType);

        // Assert the number of tickets retrieved
        assertEquals(2, ticketList.size());
    }

    @Test
    @DisplayName("Get User Tickets - Should Return User Tickets")
    public void testGetUserTickets_ShouldReturnUserTickets() throws Exception {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("userId", "Melinda test");

        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders.post("/tickets/tickets_user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseJson = getResult.getResponse().getContentAsString();
        TypeFactory typeFactory = objectMapper.getTypeFactory();
        CollectionType listType = typeFactory.constructCollectionType(List.class, TicketDto.class);
        List<TicketDto> ticketList = objectMapper.readValue(responseJson, listType);

        assertEquals(1, ticketList.size());
    }
    @Test
    @DisplayName("Get Ticket by ID - Should Return Ticket")
    public void testGetTicketById_ShouldReturnTicket() throws Exception {
        Long ticketIdToFetch = 1L;

        MvcResult getResult = mockMvc.perform(MockMvcRequestBuilders.get("/tickets/" + ticketIdToFetch))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseJson = getResult.getResponse().getContentAsString();
        TicketDto ticketDto = objectMapper.readValue(responseJson, TicketDto.class);

        assertEquals(ticketIdToFetch, ticketDto.getId());
    }
    @Test
    @DisplayName("Delete Ticket by ID - Should Delete Ticket")
    public void testDeleteTicketById_ShouldDeleteTicket() throws Exception {

        Long ticketId = 1L;

        MvcResult deleteResult = mockMvc.perform(MockMvcRequestBuilders.delete("/tickets/" + ticketId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String deleteResponse = deleteResult.getResponse().getContentAsString();

        assertEquals("Ticket deleted", deleteResponse);
    }

}
