package com.marketplace.kafka.starter.data

import com.marketplace.kafka.starter.application.Address
import com.marketplace.kafka.starter.application.TestDto
import reactor.kafka.sender.SenderRecord

import java.time.LocalDateTime
import java.util.stream.IntStream

class TestDataUtils {

    static String uuid() {
        return UUID.randomUUID().toString()
    }

    static Address mockedAddress() {
        return new Address().tap {
            city = "LA"
            street = uuid()
            registeredAt = LocalDateTime.now()
        }
    }

    static TestDto singleItem() {
        return TestDto.builder()
                .id("test-1")
                .username("test")
                .address(mockedAddress())
                .createdAt(LocalDateTime.now())
                .build()
    }

    static TestDto item(int i) {
        return new TestDto().tap {
            id = uuid() + "-" + i
            username = uuid() + "-" + i
            createdAt = LocalDateTime.now().plusMinutes(i)
        }
    }

    static List<SenderRecord<String, ?, Object>> batchOfRecords(int size, String topic) {
        return IntStream.range(0, size).mapToObj(i-> {
            TestDto dto = item(i)
            return SenderRecord.create(topic, 0, System.currentTimeMillis(), dto.getId(), dto, null)
        })
                .toList()
    }
}
