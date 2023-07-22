package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
