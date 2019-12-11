package com.sebastian_daschner.coffee_shop.systems;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javax.json.bind.annotation.JsonbCreator;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/resources/orders")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface CoffeeOrderSystem {
    
    @GET
    public List<OrderTeaser> getOrders();
    
    @PUT
    @Path("{id}")
    public void updateOrder(@PathParam("id") UUID id, Order order);

    @GET
    @Path("{id}")
    public Order getOrder(@PathParam("id") UUID id);

    @POST
    public Response createOrder(Order json);
    
    public static class OrderTeaser {
        public String _self;
        public String origin;
        public String status;
        
        @JsonbCreator
        public OrderTeaser() {}
        
        public OrderTeaser(String url, String origin, String status) {
            this._self = url;
            this.origin = origin;
            this.status = status;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof OrderTeaser))
                return false;
            OrderTeaser other = (OrderTeaser) o;
            return Objects.equals(_self, other._self) &&
                    Objects.equals(origin, other.origin) &&
                    Objects.equals(status, other.status);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(_self, origin, status);
        }
    }
    
    public static class Order {

        public String id;
        public String type;
        public String origin;
        public String status;

        @JsonbCreator
        public Order() { }

        public Order(String type, String origin) {
            this.type = type;
            this.origin = origin;
        }
        
        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Order))
                return false;
            Order other = (Order) o;
            return Objects.equals(id, other.id) &&
                    Objects.equals(origin, other.origin) &&
                    Objects.equals(status, other.status) &&
                    Objects.equals(type, other.type);
        }
        
        @Override
        public int hashCode() {
            return Objects.hash(id, origin, status, type);
        }
    }
    
}
