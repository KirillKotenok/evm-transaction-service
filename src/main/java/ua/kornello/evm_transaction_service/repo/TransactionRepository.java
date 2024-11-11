package ua.kornello.evm_transaction_service.repo;

import org.hibernate.query.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kornello.evm_transaction_service.entity.BlockTransaction;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<BlockTransaction, Long> {
    Optional<BlockTransaction> findTopByOrderByBlockNumberDesc();
}
