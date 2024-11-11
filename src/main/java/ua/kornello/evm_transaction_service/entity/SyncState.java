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
@Entity
@Table(name = "sync_state")
@EqualsAndHashCode(of = "lastProcessedBlock")
public class SyncState {
    @Id
    @Column(name = "last_processed_block", nullable = false)
    private Long lastProcessedBlock;

    public Long getLastProcessedBlock() {
        if (isNull(lastProcessedBlock)) {
            lastProcessedBlock = 0L;
        }
        return lastProcessedBlock;
    }
}
