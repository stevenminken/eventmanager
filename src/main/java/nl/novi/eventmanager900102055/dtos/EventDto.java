package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.*;
import nl.novi.eventmanager900102055.models.Artist;
import nl.novi.eventmanager900102055.models.Location;
import nl.novi.eventmanager900102055.models.Ticket;
import nl.novi.eventmanager900102055.models.User;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public class EventDto {
    private Long id;
    @Size(min = 2, message = "name event should have at least 2 characters")
    private String name;
    @NotNull(message = "Date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd") // Optional, specify the expected date format
    private LocalDate date;
    @Min(0)
    private Integer availability;
    @Min(0)
    private Integer ticketsSold;
    private Location location;
    private List<Artist> artistList;

    private List<Ticket> ticketList;
    private byte[] documentData;

    public EventDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getAvailability() {
        return availability;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Integer getTicketsSold() {
        return ticketsSold;
    }

    public void setTicketsSold(Integer ticketsSold) {
        this.ticketsSold = ticketsSold;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }
}
