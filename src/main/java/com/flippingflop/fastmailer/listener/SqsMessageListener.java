package com.flippingflop.fastmailer.listener;

import com.flippingflop.fastmailer.listener.dto.EmailSendMessage;
import com.flippingflop.fastmailer.service.EmailService;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;

@Log4j2
@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    final Gson gson;
    final EmailService emailService;

    @SqsListener("${listener.sqs.queue.email-send}")
    public void listenEmailSend(List<String> messageList) {
        log.info("Email send message received: " + messageList);

        messageList.forEach(
                messageStr -> {
                    EmailSendMessage message = gson.fromJson(messageStr, EmailSendMessage.class);

                    emailService.queueTemplateEmail(
                            message.getRecipientEmail(),
                            message.getSenderEmail(),
                            message.getTemplateName(),
                            message.getTemplateData(),
                            message.getUniqueKey()
                    );
                }
        );

    }

}
