package com.flippingflop.fastmailer.repository;

import com.flippingflop.fastmailer.test.MysqlTestContainerConfig;
import com.flippingflop.fastmailer.test.TestUtils;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = MysqlTestContainerConfig.MysqlTestContainerInitializer.class)
@Transactional
class EmailTemplateRepositoryTest {

    @Autowired
    TestUtils testUtils;

    @Autowired
    EmailTemplateRepository emailTemplateRepository;

    @Test
    void findByTemplateNameAndIsDeleted() {
        /* given */
        EmailTemplate emailTemplate = testUtils.saveEmailTemplate();

        /* when */
        EmailTemplate result = emailTemplateRepository.findByTemplateNameAndIsDeleted(emailTemplate.getTemplateName(), false);

        /* then */
        assertEquals(emailTemplate.getSubject(), result.getSubject());
    }

}