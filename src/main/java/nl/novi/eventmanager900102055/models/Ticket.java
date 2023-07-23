package nl.novi.eventmanager900102055.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double price;
    @JsonIgnore
    @OneToOne(mappedBy = "ticket", cascade = CascadeType.ALL)
    private Transaction transaction;
    @JsonIgnore
    @ManyToOne
    private Event event;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Ticket() {
    }

    public Ticket(User user, Event event, Double price) {
        this.user = user;
        this.event = event;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "event=" + event +
                ", user=" + user +
                ", price=" + price +
                '}';
    }
}
