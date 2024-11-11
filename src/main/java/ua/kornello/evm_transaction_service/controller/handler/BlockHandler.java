package ua.kornello.evm_transaction_service.controller.handler;

import io.reactivex.disposables.Disposable;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.DefaultBlockParameterNumber;
import ua.kornello.evm_transaction_service.repo.SyncStateRepository;
import ua.kornello.evm_transaction_service.service.TransactionProducerService;

import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class BlockHandler {

    private final Web3j web3j;
    private final SyncStateRepository syncStateRepository;
    private final TransactionProducerService producerService;
    private Disposable blockSubscription;


    @PostConstruct
    public void start() {
        startListening();
    }


    /**
     * Method that start processing blocks from the last one in db and to the last present
     */
    private void startListening() {
        //I want start from 0 by default but there is no transactions
        Long lastProcessedBlock = syncStateRepository.findLastProcessedBlock();

        blockSubscription = web3j.replayPastBlocksFlowable(new DefaultBlockParameterNumber(lastProcessedBlock), DefaultBlockParameterName.LATEST, true)
                .concatWith(web3j.blockFlowable(true))
                .doOnError(error -> log.error("Error in blockFlowable subscription, retrying: ", error))
                .retryWhen(errors -> errors.delay(5, TimeUnit.SECONDS))
                .subscribe(producerService::publishTransaction, error -> log.error("Error occurred while processing block:", error));

    }

    @PreDestroy
    public void stop() {
        if (blockSubscription != null && !blockSubscription.isDisposed()) {
            blockSubscription.dispose();
        }
    }
}
