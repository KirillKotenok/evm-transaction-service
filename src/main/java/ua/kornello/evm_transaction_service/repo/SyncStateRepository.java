package ua.kornello.evm_transaction_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.kornello.evm_transaction_service.entity.SyncState;

import java.util.Optional;

public interface SyncStateRepository extends JpaRepository<SyncState, Long> {
    Optional<SyncState> findTopByOrderByIdDesc();
}
