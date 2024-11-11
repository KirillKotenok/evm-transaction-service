package ua.kornello.evm_transaction_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.kornello.evm_transaction_service.entity.SyncState;

public interface SyncStateRepository extends JpaRepository<SyncState, Long> {

    @Query("SELECT s.lastProcessedBlock FROM SyncState s WHERE s.lastProcessedBlock IS NOT NULL")
    Long findLastProcessedBlock();

    @Modifying
    @Query("UPDATE SyncState s SET s.lastProcessedBlock = :lastProcessedBlock WHERE s.lastProcessedBlock IS NOT NULL")
    void updateLastProcessedBlock(@Param("lastProcessedBlock") Long lastProcessedBlock);
}
