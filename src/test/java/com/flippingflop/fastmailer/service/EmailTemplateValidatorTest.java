package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.SaveEmailTemplateRequestTest;
import com.flippingflop.fastmailer.test.MysqlTestContainerConfig;
import com.flippingflop.fastmailer.test.TestUtils;
import org.junit.jupiter.api.Nested;
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
class EmailTemplateValidatorTest {

    @Autowired
    TestUtils testUtils;
    @Autowired
    EmailTemplateValidator emailTemplateValidator;

    @Nested
    class saveEmailTemplateValidate {

        @Test
        void templateNameDuplicated() {
            /* setup */
            EmailTemplate duplicate = testUtils.saveEmailTemplate();

            /* given */
            SaveEmailTemplateRequestTest req = new SaveEmailTemplateRequestTest();
            req.templateNameDuplicated(duplicate.getTemplateName());

            /* when */
            CustomValidationException e = assertThrows(CustomValidationException.class,
                    () -> emailTemplateValidator.saveEmailTemplateValidate(req)
            );
            assertEquals(1, e.getCode());
        }

        @Test
        void requiredVariablesNotProvided() {
            /* given */
            // key not provided
            SaveEmailTemplateRequestTest req1 = new SaveEmailTemplateRequestTest();
            req1.requiredVariablesNotProvided("key");

            // default value not provided
            SaveEmailTemplateRequestTest req2 = new SaveEmailTemplateRequestTest();
            req2.requiredVariablesNotProvided("value");

            /* when */
            CustomValidationException e1 = assertThrows(CustomValidationException.class,
                    () -> emailTemplateValidator.saveEmailTemplateValidate(req1));
            assertEquals(2, e1.getCode());

            CustomValidationException e2 = assertThrows(CustomValidationException.class,
                    () -> emailTemplateValidator.saveEmailTemplateValidate(req2));
            assertEquals(2, e2.getCode());
        }

        @Test
        void keyNotConsumed() {
            /* given */
            SaveEmailTemplateRequestTest req = new SaveEmailTemplateRequestTest();
            req.keyNotConsumed();

            /* when */
            CustomValidationException e = assertThrows(CustomValidationException.class,
                    () -> emailTemplateValidator.saveEmailTemplateValidate(req));
            assertEquals(3, e.getCode());
        }

    }

}