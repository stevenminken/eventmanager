package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
