package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.test.MysqlTestContainerConfig;
import com.flippingflop.fastmailer.test.TestUtils;
import com.flippingflop.fastmailer.util.SequenceUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(initializers = MysqlTestContainerConfig.MysqlTestContainerInitializer.class)
@Transactional
class EmailValidatorTest {

    @Autowired
    TestUtils testUtils;
    @Autowired
    EmailValidator emailValidator;

    @Nested
    class sendTemplateEmailValidate {

        @Test
        void requiredVariableNotProvided() {
            // Recipient email not provided
            CustomValidationException e1 = assertThrows(CustomValidationException.class,
                    () -> emailValidator.sendTemplateEmailValidate(
                            "",
                            null,
                            SequenceUtils.getString(12),
                            null,
                            null
                    )
            );
            assertEquals(1, e1.getCode());

            // Template name not provided
            CustomValidationException e2 = assertThrows(CustomValidationException.class,
                    () -> emailValidator.sendTemplateEmailValidate(
                            SequenceUtils.getString(12) + "@example.com",
                            null,
                            "",
                            null,
                            null
                    )
            );
            assertEquals(1, e2.getCode());
        }

        @Test
        void templateNameNotExists() {
            CustomValidationException e = assertThrows(CustomValidationException.class,
                    () -> emailValidator.sendTemplateEmailValidate(
                            SequenceUtils.getString(12) + "@example.com",
                            null,
                            SequenceUtils.getString(12),
                            null,
                            null
                    ));
            assertEquals(2, e.getCode());
        }

        @Test
        void invalidKeyProvided() {
            /* setup */
            EmailTemplate emailTemplate = testUtils.saveEmailTemplate();

            /* given */
            Map<String, String> variablesMap = new HashMap<>();
            variablesMap.put(SequenceUtils.getString(12), SequenceUtils.getString(12));

            /* when */
            CustomValidationException e = assertThrows(CustomValidationException.class,
                    () -> emailValidator.sendTemplateEmailValidate(
                            SequenceUtils.getString(12) + "@example.com",
                            null,
                            emailTemplate.getTemplateName(),
                            variablesMap,
                            null
                    ));
            assertEquals(3, e.getCode());
        }

    }

}