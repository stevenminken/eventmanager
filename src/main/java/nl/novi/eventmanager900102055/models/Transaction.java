package nl.novi.eventmanager900102055.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "date_of_purchase")
    private LocalDate dateOfPurchase;
    @JoinColumn(name = "payment_method")
    private String paymentMethod;
    @OneToOne
    private Ticket ticket;

    public Transaction() {
    }

    public Transaction(LocalDate dateOfPurchase, String paymentMethod) {
        this.dateOfPurchase = dateOfPurchase;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateOfPurchase() {
        return dateOfPurchase;
    }

    public void setDateOfPurchase(LocalDate dateOfPurchase) {
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

    @Override
    public String toString() {
        return "Transaction{" +
                "dateOfPurchase=" + dateOfPurchase +
                '}';
    }
}
