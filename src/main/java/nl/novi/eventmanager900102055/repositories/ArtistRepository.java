package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Artist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByName(String name);
}
