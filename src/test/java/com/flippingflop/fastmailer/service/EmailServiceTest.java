package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.model.enums.email.EmailStatus;
import com.flippingflop.fastmailer.model.vo.Email;
import com.flippingflop.fastmailer.model.vo.EmailSendAttempt;
import com.flippingflop.fastmailer.repository.EmailRepository;
import com.flippingflop.fastmailer.repository.EmailSendAttemptRepository;
import com.flippingflop.fastmailer.test.MysqlTestContainerConfig;
import com.flippingflop.fastmailer.test.TestUtils;
import com.flippingflop.fastmailer.util.SesUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
        @Mock
        EmailSendAttemptRepository emailSendAttemptRepositoryMock;

        @Test
        void success() {
            /* given */
            Email email = testUtils.createEmail();

            /* when */
            emailServiceMock.sendTemplateEmail(email);

            /* then */
            verify(emailValidatorMock).sendTemplateEmailValidate(email);
            verify(sesUtilsMock).sendTemplateEmail(anyString(), anyString(), anyString(), any());
            verify(emailRepositoryMock).save(email);
            verify(emailSendAttemptRepositoryMock).save(any(EmailSendAttempt.class));
        }

        @Test
        void fail_sesRequestFail() {
            /* given */
            Email email = testUtils.createEmail();

            // Mock ses request failure.
            RuntimeException mockException = new RuntimeException("Ses email send request failed.");
            doThrow(mockException).when(sesUtilsMock).sendTemplateEmail(anyString(), anyString(), anyString(), any());

            // Capture the entities for assertions.
            ArgumentCaptor<Email> emailCaptor = ArgumentCaptor.forClass(Email.class);
            ArgumentCaptor<EmailSendAttempt> attemptCaptor = ArgumentCaptor.forClass(EmailSendAttempt.class);

            /* when */
            emailServiceMock.sendTemplateEmail(email);

            /* then */
            // Email status is updated to FAIL
            verify(emailRepositoryMock).save(emailCaptor.capture());
            Email capturedEmail = emailCaptor.getValue();
            assertEquals(EmailStatus.FAIL, capturedEmail.getStatus());

            // EmailSendAttempt contains exception stacktrace.
            verify(emailSendAttemptRepositoryMock).save(attemptCaptor.capture());
            EmailSendAttempt capturedAttempt = attemptCaptor.getValue();
            assertNotNull(capturedAttempt.getExceptionTrace());
        }

    }

}