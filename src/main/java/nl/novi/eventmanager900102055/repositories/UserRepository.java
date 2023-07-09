package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Iterable<User> findByName(String name);

}
