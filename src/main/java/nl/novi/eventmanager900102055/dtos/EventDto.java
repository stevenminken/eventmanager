package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

public class EventDto {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private LocalDate date;
    @NotNull
    private Integer availability;
    @NotNull
    private Integer ticketsSold;

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
}
