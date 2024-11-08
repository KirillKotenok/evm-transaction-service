package ua.kornello.evm_transaction_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kornello.evm_transaction_service.entity.NodeTransaction;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<NodeTransaction, Long> {
    Optional<NodeTransaction> findTopByOrderByBlockNumberDesc();
}
