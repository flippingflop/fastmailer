package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.rest.dto.ApiResponse;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.SaveEmailTemplateRequest;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.SaveEmailTemplateRequestTest;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.SaveEmailTemplateResponse;
import com.flippingflop.fastmailer.test.MysqlTestContainerConfig;
import com.flippingflop.fastmailer.util.SesUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

}