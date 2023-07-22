package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Ticket;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    @EntityGraph(attributePaths = {"event", "user"})
    List<Ticket> findAll();

    @Query("SELECT t FROM Ticket t JOIN FETCH t.event JOIN FETCH t.user WHERE t.id = :ticketId")
    Ticket findTicketByIdWithEagerFetch(@Param("ticketId") Long ticketId);
}