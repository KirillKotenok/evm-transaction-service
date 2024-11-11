package ua.kornello.evm_transaction_service.config;

import com.google.gson.Gson;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;

@Data
@ConfigurationProperties(prefix = "evm")
public class EvmConfiguration {
    private String nodeUrl;
    private String apiKey;
    private boolean includeRawResponse;

    @Bean
    public Web3j web3j() throws ConnectException {
        WebSocketService web3jService = new WebSocketService(nodeUrl + apiKey, includeRawResponse);
        web3jService.connect();
        return Web3j.build(web3jService);
    }
}
