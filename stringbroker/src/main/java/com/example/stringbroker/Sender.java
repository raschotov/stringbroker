package com.example.springbrokerapp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Sender extends MessageService implements Runnable{
    private final static Logger log = LoggerFactory.getLogger(Sender.class);

    public Sender(String queueName, boolean isTransacted, String brokerUrl, int modeSession, String messageText) {
        super(queueName, isTransacted, brokerUrl, modeSession, messageText);
    }

    @Override
    public void run() {
        send();
    }

    public void send() {
        log.info("start 'send' method");
        Connection connection = null;
        Session session = null;
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                    ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, brokerUrl);
            log.info("Created connection factory to " + brokerUrl);

            connection = connectionFactory.createConnection();
            log.info("Created connection");

            session = connection.createSession(isTransacted, modeSession);
            log.info("Created session");

            Queue queue = session.createQueue(queueName);
            MessageProducer producer = session.createProducer(queue);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage message = session.createTextMessage(messageText);

            producer.send(message);

            if(isTransacted) {
                session.commit();
            }
            log.info("Sent the message: " + message.getText());

            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
