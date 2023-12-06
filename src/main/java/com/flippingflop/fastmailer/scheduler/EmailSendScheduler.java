package com.flippingflop.fastmailer.scheduler;

import com.flippingflop.fastmailer.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@RequiredArgsConstructor
@Component
@ConditionalOnProperty(name = "scheduler.email-send.enabled", havingValue = "true")
public class EmailSendScheduler {

    final EmailService emailService;

    @Scheduled(fixedDelay = 1000)
    public void execute() {
        emailService.sendAllPendingEmails();
    }

}
