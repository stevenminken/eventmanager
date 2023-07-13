package nl.novi.eventmanager900102055.dtos;

import jakarta.validation.constraints.NotNull;
import nl.novi.eventmanager900102055.models.Event;
import nl.novi.eventmanager900102055.models.Transaction;
import nl.novi.eventmanager900102055.models.User;

public class TicketDto {
    private Long id;
    private User user;
    private Event event;
    private Transaction transaction;
    private Double price;

    public TicketDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
