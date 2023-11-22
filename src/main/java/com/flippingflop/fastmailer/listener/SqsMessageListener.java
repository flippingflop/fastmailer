package com.flippingflop.fastmailer.listener;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class SqsMessageListener {

    @SqsListener("${listener.sqs.queue.email-send}")
    public void listenEmailSend(String message) {
        System.out.println("message is received: " + message);
    }

}
