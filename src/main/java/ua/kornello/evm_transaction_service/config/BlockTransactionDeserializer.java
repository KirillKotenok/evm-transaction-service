package ua.kornello.evm_transaction_service.config;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import ua.kornello.evm_transaction_service.dto.BlockTransactionDto;

import java.nio.charset.StandardCharsets;

@Slf4j
public class BlockTransactionDeserializer implements Deserializer<BlockTransactionDto> {

    private final Gson gson;

    public BlockTransactionDeserializer() {
        gson = new Gson();
    }

    @Override
    public BlockTransactionDto deserialize(String s, byte[] bytes) {
        String json = new String(bytes, StandardCharsets.UTF_8);
        log.debug("Received JSON for deserialization: {}", json);
        return gson.fromJson(json, BlockTransactionDto.class);
    }
}
