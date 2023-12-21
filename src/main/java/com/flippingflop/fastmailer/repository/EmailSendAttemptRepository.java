package com.flippingflop.fastmailer.repository;

import com.flippingflop.fastmailer.model.vo.EmailSendAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailSendAttemptRepository extends JpaRepository<EmailSendAttempt, Long> {
}
