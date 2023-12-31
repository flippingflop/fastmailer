package com.flippingflop.fastmailer.util;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

@Component
@RequiredArgsConstructor
public class SqsUtils {

    final SqsClient sqsClient;

    /**
     * Delete SQS message.
     * @param message The message to delete
     */
    public void deleteMessage(Message<?> message) {
        MessageHeaders headers = message.getHeaders();
        String receiptHandle = headers.get("Sqs_ReceiptHandle", String.class);
        String queueUrl = headers.get("Sqs_QueueUrl", String.class);

        this.deleteMessage(receiptHandle, queueUrl);
    }

    /**
     * Delete SQS message.
     * @param receiptHandle Header "Sqs_ReceiptHandle" of the message
     * @param queueUrl The url of the queue
     */
    public void deleteMessage(String receiptHandle, String queueUrl) {
        DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder().queueUrl(queueUrl).receiptHandle(receiptHandle).build();
        sqsClient.deleteMessage(deleteMessageRequest);
    }

}
