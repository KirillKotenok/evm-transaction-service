package ua.kornello.evm_transaction_service.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "transactions")
@EqualsAndHashCode(of = "hash")
public class NodeTransaction {

    @Id
    @Column(length = 66, unique = true)
    private String hash;

    @Column(name = "from_address", length = 42)
    private String fromAddress;

    @Column(name = "to_address", length = 42)
    private String toAddress;

    @Column(name = "value", precision = 30, scale = 0)
    private BigInteger value;

    @Column(name = "block_number")
    private BigInteger blockNumber;

    @Column(name = "raw_block_number")
    private String rawBlockNumber;

    @Column(name = "timestamp")
    private Timestamp timestamp;
}
