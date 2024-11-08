package ua.kornello.evm_transaction_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static java.util.Objects.isNull;

@Data
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "sync_state")
public class SyncState {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sync_state_generator")
    @SequenceGenerator(name = "sync_state_generator", sequenceName = "sync_state_generator", allocationSize = 50)
    private Long id;
    @Column(name = "last_processed_block", nullable = false)
    private Long lastProcessedBlock;

    public Long getLastProcessedBlock() {
        if (isNull(lastProcessedBlock)) {
            lastProcessedBlock = 0L;
        }
        return lastProcessedBlock;
    }
}
