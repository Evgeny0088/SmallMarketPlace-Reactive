package com.marketplace.test.helper.starter.testContainers

import com.marketplace.test.helper.starter.common.Constants
import org.testcontainers.containers.PostgreSQLContainer

class PostgresContainerFactory implements Constants {

    private static PostgreSQLContainer postgreSQLContainer

    static PostgreSQLContainer getPostgresContainer(){

        if (postgreSQLContainer != null) return postgreSQLContainer

        postgreSQLContainer = new PostgreSQLContainer(POSTGRES_CONTAINER_IMAGE)
                .withDatabaseName(POSTGRES_DATABASE_NAME)
                .withUsername(POSTGRES_DATABASE_NAME)
                .withPassword(POSTGRES_DATABASE_NAME)

        postgresContainer.start()
        System.setProperty(POSTGRES_CONTAINER_DATASOURCE_URL, postgresContainer.getJdbcUrl())
        System.setProperty(POSTGRES_TEST_PORT, String.valueOf(postgreSQLContainer.getMappedPort((int) postgreSQLContainer.getExposedPorts().get(0))))
        System.setProperty(POSTGRES_TEST_HOST, postgreSQLContainer.getHost())
        return postgresContainer
    }
}
