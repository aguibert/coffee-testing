package com.sebastian_daschner.coffee_shop;

import org.microshed.testing.SharedContainerConfiguration;
import org.microshed.testing.testcontainers.MicroProfileApplication;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class EnvConfig implements SharedContainerConfiguration {
    
    @Container
    public static MicroProfileApplication<?> app = new MicroProfileApplication<>("coffee-shop")
        .withNetworkAliases("coffee-shop")
        .withExposedPorts(9080)
        .withAppContextRoot("coffee-shop")
        .withReadinessPath("/coffee-shop/resources");
    
    @Container
    public static PostgreSQLContainer<?> db = new PostgreSQLContainer<>()
        .withNetworkAliases("coffee-shop-db")
        .withUsername("postgres")
        .withPassword("postgres");
    
    @Container
    public static GenericContainer<?> barista = new GenericContainer<>("rodolpheche/wiremock:2.6.0")
        .withNetworkAliases("barista")
        .withCommand("--port", "9080")
        .withExposedPorts(9080);

}
