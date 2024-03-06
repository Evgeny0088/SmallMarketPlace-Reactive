package com.marketplace.kafka.starter.config;

import com.marketplace.exception.lib.exception.CustomException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import reactor.kafka.receiver.ReceiverRecord;

@Getter
public class ReceiverRecordException extends CustomException {

    private final ReceiverRecord<String, ?> record;

    public ReceiverRecordException(ReceiverRecord<String, ?> record) {
        super(HttpStatus.INTERNAL_SERVER_ERROR);
        this.record = record;
    }
}
