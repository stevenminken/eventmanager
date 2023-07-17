package nl.novi.eventmanager900102055.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private LocalDate date;
    private Integer availability;
    @Column(name = "tickets_sold")
    private Integer ticketsSold;
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "artist_list")
    private List<Artist> artistList;
    @OneToMany(mappedBy = "event")
    private List<Ticket> ticketList;

    public Event() {
    }

    public Event(String name, LocalDate date, Integer availability, Integer ticketsSold) {
        this.name = name;
        this.date = date;
        this.availability = availability;
        this.ticketsSold = ticketsSold;
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

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public String getDetails() {
        return "details";
    }

}
