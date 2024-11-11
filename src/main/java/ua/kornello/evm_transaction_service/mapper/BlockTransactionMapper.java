package ua.kornello.evm_transaction_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.NullValueMappingStrategy;
import org.web3j.protocol.core.methods.response.Transaction;
import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;
import ua.kornello.evm_transaction_service.entity.BlockTransaction;

import java.math.BigInteger;
import java.sql.Timestamp;

@Mapper(componentModel = "spring", imports = {BigInteger.class, Timestamp.class})
public interface BlockTransactionMapper {
    BlockTransaction toEntity(BlockTransactionDto dto);

    BlockTransactionDto toDto(BlockTransaction entity);

    @Mapping(source = "from", target = "fromAddress")
    @Mapping(source = "to", target = "toAddress")
    @Mapping(source = "value", target = "value", qualifiedByName = "stringToBigInteger")
    @Mapping(source = "blockNumber", target = "blockNumber", qualifiedByName = "stringToBigInteger")
    @Mapping(source = "blockNumber", target = "rawBlockNumber")
    BlockTransactionDto toBlockTransactionDto(Transaction transaction);

    @Named("stringToBigInteger")
    default BigInteger stringToBigInteger(String value) {
        return (value != null) ? new BigInteger(value) : null;
    }
}
