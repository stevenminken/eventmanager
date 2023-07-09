package nl.novi.eventmanager900102055.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String address;
    private String phonenumber;
    @OneToMany(mappedBy = "user")
    private List<Event> eventList;
    @OneToMany(mappedBy = "user")
    private List<Ticket> ticketList;
    @OneToMany(mappedBy = "user")
    private List<Location> locationList;
    @OneToMany(mappedBy = "user")
    private List<Artist> artistList;

    public User() {
    }
//    buyTickets()
//    downloadTickets();
//    addEvent()
//        editEvent()
//            removeEvent()
//                addArtist()
//                    editArtist()
//                        removeArtist()
//                            addLocation()
//                                editLocation()
//                                    removeLocation()
//                                        viewStatistics()
//                                            uploadContract()



    public User(String username, String password, String name, String email, String address, String phonenumber) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.address = address;
        this.phonenumber = phonenumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phonenumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phonenumber = phonenumber;
    }

    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    public String getDetails() {
        return "details";
    }
}
