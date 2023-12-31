package com.flippingflop.fastmailer.listener;

import com.flippingflop.fastmailer.listener.dto.EmailSendMessage;
import com.flippingflop.fastmailer.service.EmailService;
import com.flippingflop.fastmailer.util.SqsUtils;
import com.google.gson.Gson;
import io.awspring.cloud.sqs.annotation.SqsListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Log4j2
@Component
@RequiredArgsConstructor
public class SqsMessageListener {

    final SqsUtils sqsUtils;
    final Gson gson;
    final EmailService emailService;

    /**
     * Subscribes the email-send messages from SQS.
     * It converts the message into the email entity and persists as pending state.
     * SQS message deletion request is being sent after processing each message.
     */
    @SqsListener("${listener.sqs.queue.email-send}")
    public void listenEmailSend(List<Message<String>> messageList) {
        messageList.forEach(
                sqsMessage -> {
                    try {
                        EmailSendMessage message = gson.fromJson(sqsMessage.getPayload(), EmailSendMessage.class);

                        emailService.queueTemplateEmail(
                                message.getRecipientEmail(),
                                message.getSenderEmail(),
                                message.getTemplateName(),
                                message.getTemplateData(),
                                message.getUniqueKey()
                        );

                        sqsUtils.deleteMessage(sqsMessage);
                    } catch (RuntimeException e) {
                        log.error(retrieveMessageDebugInfo(sqsMessage) +
                                "\n[trace]: " + e.getMessage()
                        );
                    }
                }
        );
    }

    /**
     * Retrieve information that are needed for debugging from message.
     * @param message
     * @return
     * Message ID and ReceiptHandle retrieved from message header
     */
    private String retrieveMessageDebugInfo(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String id = headers.containsKey("id") ? headers.get("id", UUID.class).toString() : null;
        String receiptHandle = headers.get("Sqs_ReceiptHandle", String.class);

        return "[SQS message Id]: " + id +
                "\n[Sqs_ReceiptHandle]: " + receiptHandle;
    }

}
