package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Musician;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicianRepository extends JpaRepository<Musician, Long> {

    Iterable<Musician> findByLastName(String lastName);

}
