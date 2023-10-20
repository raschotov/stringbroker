package com.example.springbrokerapp;

import javax.jms.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class StringBrokerApp {
    @Value("${stringBrokerApp.url_tcp}")
	private static String URL_TCP;
    @Value("${stringBrokerApp.url_vm}")
    private static String URL_VM;
    @Value("${stringBrokerApp.queue_name}")
    private static String QUEUE_NAME;
    @Value("${stringBrokerApp.is_transacted}")
    private static boolean IS_TRANSACTED;
    @Value("${stringBrokerApp.model_session}")
    private static int MODEL_SESSION = Session.CLIENT_ACKNOWLEDGE;
    @Value("${stringBrokerApp.text_message}")
    private static String TEXT_MESSAGE;

    @Autowired
	public static void main(String[] args) {
        try(AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            SpringApplication.run(StringBrokerApp.class, args);

            //Sender sender = new Sender(QUEUE_NAME, IS_TRANSACTED, URL_VM, MODEL_SESSION, TEXT_MESSAGE);
            Sender sender = context.getBean(Sender.class);
            Thread threadSender = new Thread(sender, "SenderThread");

            //Receiver receiver = new Receiver(QUEUE_NAME, IS_TRANSACTED, URL_VM, MODEL_SESSION);
            Receiver receiver = context.getBean(Receiver.class);
            Thread threadReceiver = new Thread(receiver, "ReceiverThread");

            //Broker embeddedBroker = new Broker(true, URL_VM, "broker");
            Broker embeddedBroker = context.getBean(Broker.class);
            embeddedBroker.run();
            try {
                threadSender.start();
                threadSender.join();

                threadReceiver.start();
                threadReceiver.join();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
	}
}
