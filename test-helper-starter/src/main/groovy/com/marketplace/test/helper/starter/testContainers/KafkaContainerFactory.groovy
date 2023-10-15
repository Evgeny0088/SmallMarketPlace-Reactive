package com.marketplace.test.helper.starter.testContainers

import com.marketplace.test.helper.starter.common.Constants
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.NewTopic
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.utility.DockerImageName

import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

import static org.apache.kafka.clients.CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG

class KafkaContainerFactory implements Constants {

    private static KafkaContainer kafkaContainer

    static KafkaContainer getKafkaContainer() throws ExecutionException, InterruptedException, TimeoutException {

        if (kafkaContainer != null) return kafkaContainer

        kafkaContainer = new KafkaContainer(DockerImageName.parse(KAFKA_CONTAINER_IMAGE))
        kafkaContainer.start()
        String bootstrapServer = kafkaContainer.getBootstrapServers()
        System.setProperty(KAFKA_CONTAINER_BOOTSTRAP_SERVER, bootstrapServer)
        return kafkaContainer
    }
}