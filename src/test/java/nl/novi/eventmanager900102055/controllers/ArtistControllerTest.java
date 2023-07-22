package nl.novi.eventmanager900102055.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.eventmanager900102055.dtos.ArtistDto;
import nl.novi.eventmanager900102055.services.ArtistService;
import nl.novi.eventmanager900102055.services.CustomUserDetailsService;
import nl.novi.eventmanager900102055.utils.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArtistController.class)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
public class ArtistControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    JwtUtil jwtUtil;
    @MockBean
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    ArtistService artistService;
    @MockBean
    ArtistDto artistDto;

    @BeforeEach
    public void setUp() {
        artistDto = new ArtistDto();
        artistDto.setId(1L);
        artistDto.setName("Klaas Muziek");
        artistDto.setGenre("Rock");
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Create Artist with Valid Data Should Return Created")
    public void testCreateArtist_WithValidData_ShouldReturnCreated() throws Exception {

        when(artistService.createArtist(any(ArtistDto.class))).thenReturn(artistDto);

        mockMvc.perform(post("/artists/create_artist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDto)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/Artists/Klaas%20Muziek"))
                .andExpect(content().string("Artist created: Klaas Muziek with id: " + artistDto.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Find All Artists Should Return OK")
    public void testFindAllArtists_ShouldReturnOk() throws Exception {
        when(artistService.findAllArtists()).thenReturn(Collections.singletonList(artistDto));

        mockMvc.perform(MockMvcRequestBuilders.get("/artists/find_all_artists"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(artistDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(artistDto.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Find Artist by Id Should Return OK")
    public void testFindArtistById_ShouldReturnOk() throws Exception {
        when(artistService.findArtistById(anyLong())).thenReturn(artistDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/artists/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(artistDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(artistDto.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Find Artist by Id Should Return Internal Server Error")
    public void testFindArtistById_ShouldReturnInternalServerError() throws Exception {
        doThrow(new RuntimeException("Something went wrong"))
                .when(artistService).findArtistById(anyLong());

        mockMvc.perform(MockMvcRequestBuilders.get("/artists/1"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Error occurred while fetching the artist: Something went wrong"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Find Artist by Name Should Return OK")
    public void testFindArtistByName_ShouldReturnOk() throws Exception {
        when(artistService.findArtistByName("John Doe")).thenReturn(artistDto);

        mockMvc.perform(post("/artists/find_artist_by_name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(artistDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(artistDto.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Find Artist by Name Should Return Internal Server Error")
    public void testFindArtistByName_ShouldReturnInternalServerError() throws Exception {
        doThrow(new RuntimeException("Something went wrong"))
                .when(artistService).findArtistByName("John Doe");

        mockMvc.perform(post("/artists/find_artist_by_name")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"John Doe\"}"))
                .andExpect(MockMvcResultMatchers.status().isInternalServerError())
                .andExpect(MockMvcResultMatchers.content().string("Error occurred while fetching the Artist: Something went wrong"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Update Artist with Valid Data Should Return OK")
    public void testUpdateArtist_WithValidData_ShouldReturnOk() throws Exception {
        when(artistService.updateArtist(anyLong(), any(ArtistDto.class))).thenReturn(artistDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/artists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(artistDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(artistDto.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(artistDto.getName()));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Update Artist with Invalid Data Should Return Bad Request")
    public void testUpdateArtist_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        when(artistService.updateArtist(anyLong(), any(ArtistDto.class))).thenReturn(artistDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/artists/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Collections.singletonMap("invalid", "data"))))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Delete Artist by Id Should Return OK")
    public void testDeleteArtistById_ShouldReturnOk() throws Exception {
        Mockito.when(artistService.deleteArtist(anyLong())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/artists/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Artist deleted"));
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    @DisplayName("Delete Artist by Id Should Return Bad Request")
    public void testDeleteArtistById_ShouldReturnBadRequest() throws Exception {
        when(artistService.deleteArtist(anyLong())).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/artists/1"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("Artist not deleted"));
    }
}
