package com.flippingflop.fastmailer.rest.dto.emailTemplate;

import com.flippingflop.fastmailer.model.vo.EmailTemplate;
import com.flippingflop.fastmailer.model.vo.TemplateVariable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ModifyEmailTemplateResponse {

    String templateName;

    String subject;

    String htmlContents;

    List<TemplateVariableDto> templateVariableList = new ArrayList<>();

    public ModifyEmailTemplateResponse(EmailTemplate vo) {
        this.templateName = vo.getTemplateName();
        this.subject = vo.getSubject();
        this.htmlContents = vo.getHtmlContents();

        vo.getTemplateVariableList().forEach(
                templateVariable -> this.templateVariableList.add(new TemplateVariableDto(templateVariable))
        );
    }

    @Getter
    static class TemplateVariableDto {

        String keyName;

        String defaultValue;

        TemplateVariableDto(TemplateVariable vo) {
            this.keyName = vo.getKeyName();
            this.defaultValue = vo.getDefaultValue();
        }

    }

}
