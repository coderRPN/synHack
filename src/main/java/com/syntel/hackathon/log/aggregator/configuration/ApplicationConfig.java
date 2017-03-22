package com.syntel.hackathon.log.aggregator.configuration;

import javax.ws.rs.ApplicationPath;

import org.apache.log4j.Logger;
import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.syntel.hackathon.log.aggregator.resource.ChequeResource;

@Configuration
@ApplicationPath("api")
public class ApplicationConfig extends ResourceConfig {
    //private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);
    

    public ApplicationConfig() {
     //   LOGGER.info("File path : {} ", stringProp1);
        register(ChequeResource.class);
    }
   
   
    
}
