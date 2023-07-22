package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Artist;
import nl.novi.eventmanager900102055.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String name);

}
