package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.repository.EmailTemplateRepository;
import com.flippingflop.fastmailer.rest.dto.ApiResponse;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.*;
import com.flippingflop.fastmailer.test.MysqlTestContainerConfig;
import com.flippingflop.fastmailer.test.TestUtils;
import com.flippingflop.fastmailer.util.SequenceUtils;
import com.flippingflop.fastmailer.util.SesUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@ContextConfiguration(initializers = MysqlTestContainerConfig.MysqlTestContainerInitializer.class)
class EmailTemplateServiceTest {

    @Autowired
    TestUtils testUtils;
    @Autowired
    EmailTemplateService emailTemplateService;
    @MockBean
    SesUtils sesUtilsMock;

    @Nested
    class saveEmailTemplate {

        @Test
        void saveEmailTemplate_success() {
            /* given */
            doNothing().when(sesUtilsMock).saveEmailTemplate(anyString(), anyString(), anyString(), any());

            SaveEmailTemplateRequest req = new SaveEmailTemplateRequestTest();

            /* when */
            ApiResponse<SaveEmailTemplateResponse> res = emailTemplateService.saveEmailTemplate(req);

            /* then */
            assertEquals(1, res.getStatus());

            assertNotNull(res.getData().getId());
        }

    }

    @Nested
    class loadEmailTemplate {

        @InjectMocks
        EmailTemplateService emailTemplateServiceMock;
        @Mock
        SesUtils sesUtilsMock;
        @Mock
        EmailTemplateRepository emailTemplateRepositoryMock;

        @Test
        void loadEmailTemplate_success() {
            /* given */
            String templateName = SequenceUtils.getString(12);
            String subject = SequenceUtils.getString(12);
            String htmlContents = SequenceUtils.getString(12);

            EmailTemplate templateFromSes = EmailTemplate.builder()
                    .templateName(templateName)
                    .subject(subject)
                    .htmlContents(htmlContents)
                    .build();

            EmailTemplate templateFromDb = EmailTemplate.builder()
                    .templateName(templateName)
                    .subject(subject)
                    .htmlContents(htmlContents)
                    .build();

            when(sesUtilsMock.loadEmailTemplate(eq(templateName))).thenReturn(templateFromSes);
            when(emailTemplateRepositoryMock.findByTemplateNameAndIsDeleted(any(), any())).thenReturn(templateFromDb);

            /* when */
            ApiResponse<LoadEmailTemplateResponse> res = emailTemplateServiceMock.loadEmailTemplate(templateName);

            /* then */
            assertEquals(1, res.getStatus());

            assertEquals(templateName, res.getData().getTemplateName());
            assertEquals(subject, res.getData().getSubject());
            assertEquals(htmlContents, res.getData().getHtmlContents());
            assertTrue(res.getData().isExistsInSes());
            assertTrue(res.getData().isExistsInDatabase());
        }

        @Test
        void loadEmailTemplate_templateNotExists() {
            /* given */
            when(sesUtilsMock.loadEmailTemplate(any())).thenReturn(null);
            when(emailTemplateRepositoryMock.findByTemplateNameAndIsDeleted(any(), any())).thenReturn(null);

            /* when */
            CustomValidationException e = assertThrows(
                    CustomValidationException.class,
                    () -> emailTemplateService.loadEmailTemplate(SequenceUtils.getString(12))
            );

            /* then */
            assertEquals(1, e.getCode());
        }

    }

    @Nested
    class modifyEmailTemplate {

        @InjectMocks
        EmailTemplateService emailTemplateServiceMock;
        @Mock
        EmailTemplateValidator emailTemplateValidatorMock;
        @Mock
        EmailTemplateRepository emailTemplateRepositoryMock;
        @Mock
        SesUtils sesUtilsMock;

        @Test
        void success() {
            /* given */
            ModifyEmailTemplateRequest req = new ModifyEmailTemplateRequestTest();
            EmailTemplate existingTemplate = testUtils.createEmailTemplate();

            doReturn(existingTemplate).when(emailTemplateRepositoryMock).findByTemplateNameAndIsDeleted(anyString(), any());

            /* when */
            ApiResponse<ModifyEmailTemplateResponse> res = emailTemplateServiceMock.modifyEmailTemplate(req);

            /* then */
            assertEquals(1, res.getStatus());
            assertNotNull(res.getData().getTemplateName());

            verify(emailTemplateValidatorMock).modifyEmailTemplateValidate(any());
            verify(emailTemplateRepositoryMock).findByTemplateNameAndIsDeleted(anyString(), any());
        }

    }

}