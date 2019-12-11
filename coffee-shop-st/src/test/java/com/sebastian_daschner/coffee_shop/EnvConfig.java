package com.sebastian_daschner.coffee_shop;

import org.microshed.testing.SharedContainerConfiguration;
import org.microshed.testing.testcontainers.ApplicationContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;

public class EnvConfig implements SharedContainerConfiguration {
    
    @Container
    public static ApplicationContainer app = new ApplicationContainer("coffee-shop")
        .withNetworkAliases("coffee-shop")
        .withAppContextRoot("/coffee-shop")
        .withReadinessPath("/coffee-shop/resources")
        .withExposedPorts(9080);
    
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
