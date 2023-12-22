package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.model.vo.TemplateVariable;
import com.flippingflop.fastmailer.repository.EmailTemplateRepository;
import com.flippingflop.fastmailer.rest.dto.ApiResponse;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.*;
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

    public ApiResponse<LoadEmailTemplateResponse> loadEmailTemplate(String templateName) {
        /* Load template from SES and database */
        EmailTemplate templateFromSes = sesUtils.loadEmailTemplate(templateName);
        EmailTemplate templateFromDb = emailTemplateRepository.findByTemplateNameAndIsDeleted(templateName, false);

        if (templateFromSes == null && templateFromDb == null) {
            throw new CustomValidationException(1, "Template does not exist.");
        }

        /* Template to response. The one from SES has priority. */
        EmailTemplate responseTemplate = templateFromSes != null ? templateFromSes : templateFromDb;
        LoadEmailTemplateResponse res = new LoadEmailTemplateResponse(responseTemplate, templateFromSes != null, templateFromDb != null);
        return new ApiResponse<>(1, "Template loaded.", res);
    }

    @Transactional
    public ApiResponse<ModifyEmailTemplateResponse> modifyEmailTemplate(ModifyEmailTemplateRequest req) {
        /* Validate */
        emailTemplateValidator.modifyEmailTemplateValidate(req);

        /* Modify EmailTemplate entity. */
        EmailTemplate existingTemplate = emailTemplateRepository.findByTemplateNameAndIsDeleted(req.getTemplateName(), false);
        EmailTemplate newTemplate = req.toVo();
        existingTemplate.modify(newTemplate);

        /* Delete existing TemplateVariable entities and save new ones. */
        existingTemplate.getTemplateVariableList().forEach(TemplateVariable::delete);
        newTemplate.getTemplateVariableList().forEach(
                existingTemplate::addTemplateVariable
        );

        /* Modify SES template. */
        sesUtils.modifyEmailTemplate(
                newTemplate.getTemplateName(),
                newTemplate.getSubject(),
                newTemplate.getHtmlContents(),
                null
        );

        ModifyEmailTemplateResponse res = new ModifyEmailTemplateResponse(existingTemplate.getTemplateName());
        return new ApiResponse<>(1, "Template modified.", res);
    }

}
