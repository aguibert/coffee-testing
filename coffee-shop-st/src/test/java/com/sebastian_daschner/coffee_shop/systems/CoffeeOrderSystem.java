package com.sebastian_daschner.coffee_shop.systems;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Ignore;

import com.sebastian_daschner.coffee_shop.entity.Order;

@Ignore
@Path("/resources/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CoffeeOrderSystem {
    
    @GET
    public List<Order> getOrders();
    
    @PUT
    @Path("{id}")
    public void updateOrder(@PathParam("id") UUID id, Order order);

    @GET
    @Path("{id}")
    public Order getOrder(@PathParam("id") UUID id);

    @POST
    public Response createOrder(Order json);
    
}
