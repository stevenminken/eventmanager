package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    Event findByName(String name);

}
