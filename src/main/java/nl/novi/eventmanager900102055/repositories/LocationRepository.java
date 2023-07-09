package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Iterable<Location> findByName(String name);
}
