package com.sebastian_daschner.coffee_shop;

import static com.sebastian_daschner.coffee_shop.systems.BaristaSystem.extractId;
import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.microshed.testing.jupiter.MicroShedTest;

import com.sebastian_daschner.coffee_shop.entity.Order;
import com.sebastian_daschner.coffee_shop.systems.BaristaSystem;
import com.sebastian_daschner.coffee_shop.systems.CoffeeOrderSystem;

@MicroShedTest
public class CreateOrderTest {

    @Inject
    public static CoffeeOrderSystem coffeeOrderSystem;
    
    private BaristaSystem baristaSystem = new BaristaSystem();

    @Test
    void createVerifyOrder() {
        List<Order> originalOrders = coffeeOrderSystem.getOrders();

        Order order = new Order("Espresso", "Colombia");
        Response resp = coffeeOrderSystem.createOrder(order);
        UUID created = UUID.fromString(extractId(resp.getLocation()));

        Order loadedOrder = coffeeOrderSystem.getOrder(created);
        assertThat(loadedOrder).isEqualToComparingOnlyGivenFields(order, "type", "origin");

        assertThat(coffeeOrderSystem.getOrders()).contains(order);
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
        assertThat(loadedOrder.getStatus()).isEqualTo("Preparing");

        baristaSystem.answerForOrder(orderUri, "FINISHED");

        loadedOrder = waitForProcessAndGet(orderUri, "FINISHED");
        assertThat(loadedOrder.getStatus()).isEqualTo("Finished");
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