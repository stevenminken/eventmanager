package nl.novi.eventmanager900102055.repositories;

import nl.novi.eventmanager900102055.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
