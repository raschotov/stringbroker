package com.example.springbrokerapp;

public abstract class MessageService {
    String queueName;
    boolean isTransacted;
    String brokerUrl;
    int modeSession;
    String messageText;

    // for Sender
    public MessageService(String queueName, boolean isTransacted, String brokerUrl, int modeSession, String messageText) {
        this.queueName = queueName;
        this.isTransacted = isTransacted;
        this.brokerUrl = brokerUrl;
        this.modeSession = modeSession;
        this.messageText = messageText;
    }

    // for Receiver
    public MessageService(String queueName, boolean isTransacted, String brokerUrl, int modeSession) {
        this.queueName = queueName;
        this.isTransacted = isTransacted;
        this.brokerUrl = brokerUrl;
        this.modeSession = modeSession;
    }
}
