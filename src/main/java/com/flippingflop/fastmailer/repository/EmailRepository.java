package com.flippingflop.fastmailer.repository;

import com.flippingflop.fastmailer.model.enums.email.EmailStatus;
import com.flippingflop.fastmailer.model.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailRepository extends JpaRepository<Email, Long> {

    List<Email> findAllByStatusAndIsDeleted(EmailStatus status, boolean isDeleted);

}
