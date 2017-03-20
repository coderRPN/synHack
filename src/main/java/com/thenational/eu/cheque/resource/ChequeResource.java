package com.thenational.eu.cheque.resource;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.springframework.boot.actuate.health.Health;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.thenational.eu.cheque.configuration.ApplicationContextProvider;

@Component
@Path("/")
public class ChequeResource {

    @GET
    @Produces("application/json")
    @Path("/health")
    public Health getHealth() {
        return new Health.Builder().up().withDetail("version", "0.1.0").withDetail("author", "prabhdeep").build();
    }
    
    @GET
    @Produces("application/json")
    @Path("/jms/test")
    public String triggerJmsMessage() throws InterruptedException, JMSException {
        JmsTemplate jmsTemplate = ApplicationContextProvider.getApplicationContext().getBean(JmsTemplate.class);
        
        System.out.println("Sending Jms message");
        Message ack =  jmsTemplate.sendAndReceive("requestQueue", new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage("Hello Prabhdeep");
            }
        });
        return ((TextMessage) ack).getText();
    }
}
