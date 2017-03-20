package com.syntel.hackathon.log.aggregator.resource;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

@Component
@Path("/")
public class ChequeResource {

    @GET
    @Produces("application/json")
    @Path("/health")
    public Health getHealth() {
        return new Health.Builder().up().withDetail("version", "0.1.0").withDetail("author", "prabhdeep").build();
    }
    
}
