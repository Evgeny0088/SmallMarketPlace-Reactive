package com.marketplace.test.helper.starter.common;

public interface Constants {

    /*
    wire mock config
    */
    int SERVER_PORT = 4567;
    String SERVER_HOST = "localhost";
    String CLASSPATH = "classpath:";
    String TEST_WIREMOCK_SERVER = "test-helper-wiremock-server";
    String TEST_REST_TEMPLATE = "test-rest-template";
    String WEBCLIENT_TIMEOUT = "1000000";

    /*
    containers
    */
    String KAFKA_CONTAINER_IMAGE = "confluentinc/cp-kafka:7.0.0";
    String POSTGRES_CONTAINER_IMAGE = "postgres:13";
    String REDIS_CONTAINER_IMAGE = "redis:7.0.2";

    String KAFKA_CONTAINER_BOOTSTRAP_SERVER = "spring.kafka.bootstrap-servers";
    String POSTGRES_CONTAINER_DATASOURCE_URL = "spring.r2dbc.url";
    String POSTGRES_TEST_PORT = "r2dbc-props.port";
    String POSTGRES_TEST_HOST = "r2dbc-props.host";
    String POSTGRES_DATABASE_NAME = "test";
    String REDIS_HOST = "spring.redis.host";
    String REDIS_MAPPED_PORT = "spring.redis.port";
    int REDIS_CONTAINER_PORT = 6379;
}
