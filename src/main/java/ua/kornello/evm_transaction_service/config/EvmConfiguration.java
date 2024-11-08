package ua.kornello.evm_transaction_service.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "evm")
public class EvmConfiguration {
    private String nodeUrl;
    private String apiKey;

    public String getUrl() {
        return nodeUrl + apiKey;
    }
}
