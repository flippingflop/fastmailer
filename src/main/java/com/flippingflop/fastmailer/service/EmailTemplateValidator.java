package com.flippingflop.fastmailer.service;

import com.flippingflop.fastmailer.exception.model.CustomValidationException;
import com.flippingflop.fastmailer.repository.EmailTemplateRepository;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.ModifyEmailTemplateRequest;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.SaveEmailTemplateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class EmailTemplateValidator {

    final EmailTemplateRepository emailTemplateRepository;

    public void saveEmailTemplateValidate(SaveEmailTemplateRequest req) {
        /* Check template name duplication. */
        boolean templateNameExists = emailTemplateRepository.existsByTemplateNameAndIsDeleted(req.getTemplateName(), false);
        if (templateNameExists) {
            throw new CustomValidationException(1, "Template name already in use.");
        }

        /* Check if all variable keys and default values are provided. */
        boolean requiredVarNotProvided = req.getTemplateVariableList().stream()
                .anyMatch(var -> !StringUtils.hasLength(var.getKeyName()) || !StringUtils.hasLength(var.getDefaultValue()));
        if (requiredVarNotProvided) {
            throw new CustomValidationException(2, "Required variable not provided.");
        }

        /* Check if all variable keys are consumed in htmlContents. */
        boolean keyNotConsumed = req.getTemplateVariableList().stream()
                .map(templateVariable -> templateVariable.getKeyName())
                .anyMatch(keyName -> !req.getHtmlContents().contains("{{" + keyName + "}}"));
        if (keyNotConsumed) {
            throw new CustomValidationException(3, "Variable key not consumed within HTML contents.");
        }
    }

    public void modifyEmailTemplateValidate(ModifyEmailTemplateRequest req) {

    }

}
