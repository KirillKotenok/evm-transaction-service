package ua.kornello.evm_transaction_service.service;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.http.HttpService;
import ua.kornello.evm_transaction_service.config.EvmConfiguration;

import java.io.IOException;
import java.math.BigInteger;

@Service
@RequiredArgsConstructor
public class EvmService {
    private final EvmConfiguration configuration;
    private Web3j web3j;

    @PostConstruct
    public void connectToNode() {
        this.web3j = Web3j.build(new HttpService(configuration.getUrl()));
    }

    public Long getLatestBlockNumber() throws IOException {
        EthBlock block = web3j.ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send();
        return block.getBlock().getNumber().longValue();
    }

    public EthBlock getBlockByNumber(long blockNumber) throws IOException {
        return web3j.ethGetBlockByNumber(DefaultBlockParameter.valueOf(BigInteger.valueOf(blockNumber)), true).send();
    }
}
