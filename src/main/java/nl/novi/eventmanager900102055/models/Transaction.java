package nl.novi.eventmanager900102055.models;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "tickets")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateOfPurchase;
    private String paymentMethod;
    @OneToOne
    private Ticket ticket;

    public Transaction() {
    }

    public Transaction(Date dateOfPurchase, String paymentMethod) {
        this.dateOfPurchase = dateOfPurchase;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(Date dateOfPurchase) {
        this.dateOfPurchase = dateOfPurchase;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public String getDetails() {
        return "details";
    }
}
