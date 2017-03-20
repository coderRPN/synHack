package com.thenational.eu.cheque.service;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageReceiver {

    @JmsListener(destination = "requestQueue")
    public String receive(Message message) throws JMSException {
        System.out.println("Received <" + ((TextMessage) message).getText() + ">");
        return "acknowledged";
    }
}
