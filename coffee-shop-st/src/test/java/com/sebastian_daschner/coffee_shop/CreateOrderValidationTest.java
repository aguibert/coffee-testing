package com.sebastian_daschner.coffee_shop;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import org.junit.jupiter.api.Test;
import org.microshed.testing.jupiter.MicroShedTest;

import com.sebastian_daschner.coffee_shop.entity.Order;
import com.sebastian_daschner.coffee_shop.systems.CoffeeOrderSystem;

@MicroShedTest
public class CreateOrderValidationTest {

    @Inject
    public static CoffeeOrderSystem coffeeOrderSystem;

    @Test
    void invalidEmptyOrder() {
        Response r = coffeeOrderSystem.createOrder(new Order());
        verifyClientError(r);
    }

    @Test
    void invalidEmptyCoffeeType() {
        createOrder(null, "Colombia");
    }

    @Test
    void invalidEmptyOrigin() {
        createOrder("Espresso", null);
    }

    @Test
    void invalidCoffeeType() {
        createOrder("Siphon", "Colombia");
    }
    
    @Test
    void invalidCoffeeOrigin() {
        createOrder("Espresso", "Germany");
    }

    @Test
    void invalidEmptyCoffeeTypeInvalidOrigin() {
        createOrder(null, "Germany");
    }

    @Test
    void invalidEmptyOriginInvalidCoffeeType() {
        createOrder("Siphon", null);
    }

    private void createOrder(String o, String colombia) {
        Order order = new Order(o, colombia);
        Response r = coffeeOrderSystem.createOrder(order);
        verifyClientError(r);
    }
    
    private void verifyClientError(Response response) {
        verifyStatus(response, Response.Status.Family.CLIENT_ERROR);
    }

    private void verifyStatus(Response response, Response.Status.Family clientError) {
        if (response.getStatusInfo().getFamily() != clientError)
            throw new AssertionError("Status was not successful: " + response.getStatus());
    }

}