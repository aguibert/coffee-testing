package com.sebastian_daschner.coffee_shop;

import static com.sebastian_daschner.coffee_shop.systems.BaristaSystem.extractId;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.microshed.testing.SharedContainerConfig;
import org.microshed.testing.jaxrs.RESTClient;
import org.microshed.testing.jupiter.MicroShedTest;

import com.sebastian_daschner.coffee_shop.systems.BaristaSystem;
import com.sebastian_daschner.coffee_shop.systems.CoffeeOrderSystem;
import com.sebastian_daschner.coffee_shop.systems.CoffeeOrderSystem.Order;
import com.sebastian_daschner.coffee_shop.systems.CoffeeOrderSystem.OrderTeaser;

@MicroShedTest
@SharedContainerConfig(EnvConfig.class)
public class CreateOrderTest {

    @RESTClient
    public static CoffeeOrderSystem coffeeOrderSystem;
    
    private BaristaSystem baristaSystem = new BaristaSystem();

    @Test
    void createVerifyOrder() {
        Order order = new Order("Espresso", "Colombia");
        Response resp = coffeeOrderSystem.createOrder(order);
        UUID created = UUID.fromString(extractId(resp.getLocation()));

        Order loadedOrder = coffeeOrderSystem.getOrder(created);
        assertThat(loadedOrder).isEqualToComparingOnlyGivenFields(order, "type", "origin");

        OrderTeaser orderTeaser = new OrderTeaser(resp.getLocation().toString(), loadedOrder.origin, loadedOrder.status);
        assertThat(coffeeOrderSystem.getOrders()).contains(orderTeaser);
    }

    @Test
    void createOrderCheckStatusUpdate() {
        Order order = new Order("Espresso", "Colombia");
        Response created = coffeeOrderSystem.createOrder(order);
        URI orderUri = created.getLocation();
        String orderId = extractId(orderUri);

        baristaSystem.answerForOrder(orderUri, "PREPARING");

        Order loadedOrder = coffeeOrderSystem.getOrder(UUID.fromString(orderId));
        assertThat(loadedOrder).isEqualToComparingOnlyGivenFields(order, "type", "origin");

        loadedOrder = waitForProcessAndGet(orderUri, "PREPARING");
        assertThat(loadedOrder.status).isEqualTo("Preparing");

        baristaSystem.answerForOrder(orderUri, "FINISHED");

        loadedOrder = waitForProcessAndGet(orderUri, "FINISHED");
        assertThat(loadedOrder.status).isEqualTo("Finished");
    }

    private Order waitForProcessAndGet(URI orderUri, String requestedStatus) {
        baristaSystem.waitForInvocation(orderUri, requestedStatus);
        String orderId = orderUri.toString();
        orderId = orderId.substring(orderId.lastIndexOf('/') + 1);
        return coffeeOrderSystem.getOrder(UUID.fromString(orderId));
    }

    @AfterEach 
    void reset() {
        baristaSystem.reset();
    }

}