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
@PropertySource("classpath:application.properties")
public class ApplicationConfig extends ResourceConfig {
    //private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationConfig.class);
    
    @Value("${logFilepath}")
    private String stringProp1;
    
    public String getStringProp1() {
        return stringProp1;
    }

    public void setStringProp1(String stringProp1) {
        this.stringProp1 = stringProp1;
    }

    public ApplicationConfig() {
     //   LOGGER.info("File path : {} ", stringProp1);
        register(ChequeResource.class);
    }
   
   
    
}
