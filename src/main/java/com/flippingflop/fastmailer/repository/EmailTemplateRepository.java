package com.flippingflop.fastmailer.repository;

import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailTemplateRepository extends JpaRepository<EmailTemplate, Long> {

    EmailTemplate findByTemplateNameAndIsDeleted(String templateName, Boolean isDeleted);

}
