package com.flippingflop.fastmailer.test;

import com.flippingflop.fastmailer.model.enums.email.EmailStatus;
import com.flippingflop.fastmailer.model.vo.Email;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.repository.EmailTemplateRepository;
import com.flippingflop.fastmailer.util.SequenceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Utility class for generating test entities in a consistent and controlled manner.
 * Provides a set of methods to create various entity objects for testing purposes.
 *
 * The naming convention of the methods determines their behaviour:
 *   - Method starting with "create" instantiate and return entity objects without persisting them to database (or PersistenceContext).
 *   - Method starting with "save" first persist the entity to the database and then return the persisted entity.
 *
 * Each method in this class uses the `SequenceUtils.getString(int fixedLength)` utility method to generate unique and
 * consistent field values for the entities, aiding in the creation of distinct test objects with predictable data.
 */
@Component
public class TestUtils {

    @Autowired
    private EmailTemplateRepository emailTemplateRepository;

    public EmailTemplate createEmailTemplate() {
        return EmailTemplate.builder()
                .templateName(SequenceUtils.getString(12))
                .subject(SequenceUtils.getString(12))
                .htmlContents(SequenceUtils.getString(12))
                .build();
    }

    public EmailTemplate saveEmailTemplate() {
        return emailTemplateRepository.save(createEmailTemplate());
    }

    public Email createEmail() {
        return Email.builder()
                .senderEmail(SequenceUtils.getString(12) + "@example.com")
                .recipientEmail(SequenceUtils.getString(12) + "@example.com")
                .emailTemplate(createEmailTemplate())
                .status(EmailStatus.PENDING)
                .build();
    }

}
