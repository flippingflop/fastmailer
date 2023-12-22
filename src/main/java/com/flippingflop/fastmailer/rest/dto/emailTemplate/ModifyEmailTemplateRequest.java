package com.flippingflop.fastmailer.rest.dto.emailTemplate;

import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.model.vo.TemplateVariable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ModifyEmailTemplateRequest {

    String templateName;

    String subject;

    String htmlContents;

    List<TemplateVariableDto> templateVariableList = new ArrayList<>();

    public EmailTemplate toVo() {
        EmailTemplate emailTemplate = EmailTemplate.builder()
                .templateName(templateName)
                .subject(subject)
                .htmlContents(htmlContents)
                .build();

        templateVariableList.forEach(dto -> emailTemplate.addTemplateVariable(dto.toVo()));

        return emailTemplate;
    }

    @Getter
    public static class TemplateVariableDto {

        String keyName;

        String defaultValue;

        TemplateVariable toVo() {
            return TemplateVariable.builder()
                    .keyName(keyName)
                    .defaultValue(defaultValue)
                    .build();
        }

    }

}
