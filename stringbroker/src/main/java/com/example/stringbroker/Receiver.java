package com.example.springbrokerapp;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Receiver extends MessageService implements Runnable{
    private final static Logger log = LoggerFactory.getLogger(Sender.class);

    public Receiver(String queueName, boolean isTransacted, String brokerUrl, int modeSession) {
        super(queueName, isTransacted, brokerUrl, modeSession);
    }

    @Override
    public void run() {
        receiveMessage();
    }

    public void receiveMessage() {
        log.info("start 'receiveMessage' method");
        Connection connection = null;
        Session session = null;
        try {
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnectionFactory.DEFAULT_USER, ActiveMQConnectionFactory.DEFAULT_PASSWORD, brokerUrl);
            log.info("Created connection factory to " + brokerUrl);

            connection = connectionFactory.createConnection();
            log.info("Created connection");
            connection.start();
            log.info("Connection started");

            session = connection.createSession(isTransacted, modeSession);
            log.info("Created session");

            Queue queue = session.createQueue(queueName);
            MessageConsumer consumer = session.createConsumer(queue);
            long startTime = System.currentTimeMillis();
            Message message = consumer.receive();

            TextMessage responseMessage = (TextMessage) message;
            log.info("Message: " + responseMessage.getText());

            message.acknowledge();

            System.out.println(System.currentTimeMillis()  - startTime);

            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
