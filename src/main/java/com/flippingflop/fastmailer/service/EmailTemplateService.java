package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.repository.EmailTemplateRepository;
import com.flippingflop.fastmailer.rest.dto.ApiResponse;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.SaveEmailTemplateRequest;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.SaveEmailTemplateResponse;
import com.flippingflop.fastmailer.util.SesUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmailTemplateService {

    final SesUtils sesUtils;
    final EmailTemplateValidator emailTemplateValidator;
    final EmailTemplateRepository emailTemplateRepository;

    @Transactional
    public ApiResponse<SaveEmailTemplateResponse> saveEmailTemplate(SaveEmailTemplateRequest req) {
        /* Validate */
        emailTemplateValidator.saveEmailTemplateValidate(req);

        /* Save */
        EmailTemplate emailTemplate = req.toVo();
        emailTemplateRepository.save(emailTemplate);

        /* Upload template to SES */
        sesUtils.saveEmailTemplate(req.getTemplateName(), req.getSubject(), req.getHtmlContents(), null);

        /* res */
        SaveEmailTemplateResponse res = new SaveEmailTemplateResponse(emailTemplate.getId());
        return new ApiResponse<>(1, "Template saved.", res);
    }

}
