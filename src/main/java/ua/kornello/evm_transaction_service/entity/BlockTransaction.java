package ua.kornello.evm_transaction_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "transactions")
@EqualsAndHashCode(of = "hash")
public class BlockTransaction {

    @Id
    @Column(length = 66, unique = true, nullable = false)
    private String hash;

    @Column(name = "from_address", length = 42)
    private String fromAddress;

    @Column(name = "to_address", length = 42)
    private String toAddress;

    @Column(name = "value", precision = 30)
    private BigInteger value;

    @Column(name = "block_number")
    private Long blockNumber;

    @Column(name = "raw_block_number")
    private String rawBlockNumber;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private Timestamp createdAt;
}
