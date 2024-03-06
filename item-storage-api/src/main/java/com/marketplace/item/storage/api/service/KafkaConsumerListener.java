package com.marketplace.item.storage.api.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.marketplace.exception.lib.exception.CustomException;
import com.marketplace.item.storage.api.common.Constants;
import com.marketplace.item.storage.api.dto.ItemDetails;
import com.marketplace.item.storage.api.dto.ItemTransactionRequest;
import com.marketplace.item.storage.api.enums.TransactionStatus;
import com.marketplace.item.storage.api.mapper.KafkaMapper;
import com.marketplace.item.storage.api.repository.ItemRepository;
import com.marketplace.item.storage.api.repository.RedisRepository;
import com.marketplace.item.storage.api.repository.SellTransactionRepository;
import com.marketplace.kafka.starter.config.KafkaStarterConfig;
import com.marketplace.kafka.starter.config.ReceiverRecordException;
import com.marketplace.kafka.starter.properties.KafkaTopicProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.kafka.receiver.KafkaReceiver;

import static com.marketplace.item.storage.api.common.MessageConstants.*;

@Service
@Slf4j
public class KafkaConsumerListener implements Constants {

    private final RedisRepository redisRepository;
    private final KafkaProducerService producerService;
    private final KafkaReceiver<String, ItemTransactionRequest> itemsUpdateReceiver;

    private final SellTransactionRepository sellTransactionRepository;
    private final ItemRepository itemRepository;

    private final KafkaMapper kafkaMapper;

    public KafkaConsumerListener(KafkaStarterConfig starterConfig,
                                 KafkaTopicProperties kafkaTopicProperties,
                                 RedisRepository redisRepository,
                                 KafkaProducerService producerService,
                                 SellTransactionRepository sellTransactionRepository,
                                 ItemRepository itemRepository,
                                 KafkaMapper kafkaMapper) {
        this.producerService = producerService;
        this.redisRepository = redisRepository;
        this.sellTransactionRepository = sellTransactionRepository;
        this.itemRepository = itemRepository;
        this.kafkaMapper = kafkaMapper;
        this.itemsUpdateReceiver =
                KafkaReceiver.create(starterConfig.createTypedReceiverOptions(kafkaTopicProperties.getTopics().get(ITEM_TRANSACTION_REQUEST_TOPIC), new TypeReference<>() {}));
    }

    @Transactional
    @EventListener(ApplicationReadyEvent.class)
    public Mono<Void> handleTransactionRequest() {
        return itemsUpdateReceiver.receive(1)
                .flatMap(record-> sellTransactionRepository.tryToSaveTransaction(record.value()))
                .flatMap(itemRepository::getItemDetails)
                .filter(pipeline -> {
                    ItemTransactionRequest request = pipeline.getTransactionRequest();
                    ItemDetails details = pipeline.getItemDetails();
                    if (details.getItemsCount() < request.getItemDetails().getItemsCount()) {
                        producerService.sendTransactionResponse(
                                kafkaMapper.toTransactionResponse(
                                        request.getTransactionId().toString(),
                                        String.format(SELLING_ERROR_COUNT, details.getItemsCount(), request.getItemDetails().getItemsCount()),
                                        TransactionStatus.BAD_REQUEST.name()))
                                .subscribeOn(Schedulers.boundedElastic())
                                .subscribe();
                        return false;
                    }
                    return true;
                })
                .flatMap(itemRepository::removeSoldItems)
                .flatMap(redisRepository::invalidateCacheOnItemUpdate)
                .flatMap(pipeline->
                        producerService.sendTransactionResponse(
                                kafkaMapper.toTransactionResponse(
                                        pipeline.getTransactionRequest().getTransactionId().toString(),
                                        String.format(SOLD_SUCCESSFULLY,
                                                pipeline.getTransactionRequest().getItemDetails().getItemsCount(),
                                                pipeline.getItemDetails().getItemId()),
                                        TransactionStatus.OK.name())))
                .onErrorContinue((e, record) -> {
                    if (e instanceof ReceiverRecordException recordException) {
                        log.error(String.format(FAILED_RECORD_ERROR, recordException.getRecord(), recordException.getRecord().offset()));
                        recordException.getRecord().receiverOffset().acknowledge();
                    }
                    else if (e instanceof CustomException custom) {
                        log.error(String.format(TRANSACTION_ALREADY_PROCESSED, custom.getMessage()));
                    }
                    else {
                        log.error(String.format(CONSUMER_ERROR, e.getMessage(), record));
                    }})
                .then();
    }
}