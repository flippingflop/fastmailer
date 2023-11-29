package com.flippingflop.fastmailer.listener.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class EmailSendMessage {

    String uniqueKey;

    String recipientEmail;

    String senderEmail;

    String templateName;

    Map<String, String> templateData;

}
