package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.model.enums.email.EmailStatus;
import com.flippingflop.fastmailer.model.vo.Email;
import com.flippingflop.fastmailer.repository.EmailRepository;
import com.flippingflop.fastmailer.test.MysqlTestContainerConfig;
import com.flippingflop.fastmailer.test.TestUtils;
import com.flippingflop.fastmailer.util.SesUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ContextConfiguration(initializers = MysqlTestContainerConfig.MysqlTestContainerInitializer.class)
class EmailServiceTest {

    @Autowired
    TestUtils testUtils;

    @Nested
    class sendTemplateEmail {

        @InjectMocks
        EmailService emailServiceMock;
        @Mock
        EmailValidator emailValidatorMock;
        @Mock
        EmailRepository emailRepositoryMock;
        @Mock
        SesUtils sesUtilsMock;

        @Test
        void success() {
            /* given */
            Email email = testUtils.createEmail();
            doNothing().when(emailValidatorMock).sendTemplateEmailValidate(eq(email));
            when(emailRepositoryMock.save(email)).thenReturn(email);
            doNothing().when(sesUtilsMock).sendTemplateEmail(anyString(), anyString(), anyString(), any());

            /* when */
            emailServiceMock.sendTemplateEmail(email);

            /* then */
            assertEquals(EmailStatus.SUCCESS, email.getStatus());
        }

    }

}