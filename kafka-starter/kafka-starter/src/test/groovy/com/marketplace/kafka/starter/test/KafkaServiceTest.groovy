package com.marketplace.kafka.starter.test

import com.marketplace.kafka.starter.BaseSpecification
import com.marketplace.kafka.starter.application.KafkaConsumerListener
import com.marketplace.kafka.starter.application.KafkaService
import com.marketplace.kafka.starter.application.TestDto
import com.marketplace.kafka.starter.application.User
import com.marketplace.kafka.starter.data.TestDataUtils
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import reactor.kafka.sender.KafkaSender

import java.time.LocalDateTime

@Slf4j
@DirtiesContext
class KafkaServiceTest extends BaseSpecification {

    @Autowired
    KafkaService kafkaService

    @Autowired
    KafkaConsumerListener kafkaListener

    @Autowired
    KafkaSender<String, Object> producer

    def "produce message and check if this is readable" () {
        given:
        log.info(String.format("Topics created: %s", adminClient.listTopics().names().get()))
        TestDto dto = TestDataUtils.singleItem()
        kafkaService.sendNewUser(dto).subscribe()
        List<User> users = kafkaListener.getUsers()

        expect:
        awaiter.until(()-> users.size() == 1)
        assert users.get(0).getId() == dto.getId()
        assert users.get(0).getUsername() == dto.getUsername()
        assert users.get(0).getCreatedAt() instanceof LocalDateTime

        cleanup:
        users.clear()
    }

    def "produce batch of messages and verify results, imitating error on consumer side" () {
        given:
        int size = 100
        int half = (size / 2).intValue()
        def records = TestDataUtils.batchOfRecords(size, outTopic.name())
        kafkaService.sendBatchOfUsers(records).subscribe()
        List<User> users = kafkaListener.getUsers()

        expect:
        awaiter.until(()-> users.size() == half)
        assert users.size() == half

        cleanup:
        users.clear()
    }
}
