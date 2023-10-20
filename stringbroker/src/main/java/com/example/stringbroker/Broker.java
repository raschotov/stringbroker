package com.example.springbrokerapp;

import org.apache.activemq.broker.BrokerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Broker {
    private static Logger logger = LoggerFactory.getLogger(Broker.class);
    private boolean isPersistent;
    private String url;
    private String brokerName;

    public Broker(boolean isPersistent, String url, String brokerName) {
        this.isPersistent = isPersistent;
        this.url = url;
        this.brokerName = brokerName;
    }

    public void run() {
        BrokerService brokerService = new BrokerService();
        try {
            brokerService.setPersistent(isPersistent);
            brokerService.setUseJmx(false);
            brokerService.addConnector(url);
            brokerService.setBrokerName(brokerName);

            brokerService.start();
            logger.info("Broker started");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
