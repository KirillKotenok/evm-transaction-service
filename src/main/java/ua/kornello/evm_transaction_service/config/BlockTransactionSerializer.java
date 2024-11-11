package ua.kornello.evm_transaction_service.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serializer;
import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;

@Slf4j
public class BlockTransactionSerializer implements Serializer<BlockTransactionDto> {

    private final Gson gson;

    public BlockTransactionSerializer() {
        gson = new Gson();
    }

    @Override
    public byte[] serialize(String s, BlockTransactionDto transaction) {
        String json = gson.toJson(transaction);
        log.debug("Serialized transaction: {}", json);
        return json.getBytes();
    }
}
