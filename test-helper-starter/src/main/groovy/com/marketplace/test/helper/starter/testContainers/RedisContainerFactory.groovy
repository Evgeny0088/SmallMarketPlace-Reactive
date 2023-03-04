package com.marketplace.test.helper.starter.testContainers

import com.marketplace.test.helper.starter.common.Constants
import org.testcontainers.containers.GenericContainer

class RedisContainerFactory implements Constants {

    private static GenericContainer redis_container

    static GenericContainer getRedisContainer() {
        if (redis_container != null) return redis_container

        redis_container = new GenericContainer(REDIS_CONTAINER_IMAGE)
                .withExposedPorts(REDIS_CONTAINER_PORT)

        redis_container.start()
        System.setProperty(REDIS_HOST, redis_container.getHost());
        System.setProperty(REDIS_MAPPED_PORT, redis_container.getMappedPort(REDIS_CONTAINER_PORT).toString());

        return redis_container
    }
}
