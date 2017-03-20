package com.syntel.hackathon.log.aggregator.configuration;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.context.annotation.Configuration;

import com.syntel.hackathon.log.aggregator.resource.ChequeResource;

@Configuration
@ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        register(ChequeResource.class);
    }
}