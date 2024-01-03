package com.flippingflop.fastmailer.model.vo;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@SuperBuilder
@Table(name = "email_template", indexes = {
        @Index(name = "IDX_IS_DELETED", columnList = "IS_DELETED")
})
public class EmailTemplate {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "TEMPLATE_NAME", nullable = false)
    String templateName;

    @Column(name = "SUBJECT", nullable = false)
    String subject;

    @Column(name = "HTML_CONTENTS", nullable = false, columnDefinition = "MEDIUMTEXT")
    String htmlContents;

    @CreatedDate
    @Column(name = "CREATED_AT")
    Instant createdAt;

    @Builder.Default
    @Column(name = "IS_DELETED", nullable = false)
    Boolean isDeleted = false;

    @Column(name = "DELETED_AT")
    Instant deletedAt;

    @Builder.Default
    @OneToMany(mappedBy = "emailTemplate", cascade = CascadeType.PERSIST)
    List<TemplateVariable> templateVariableList = new ArrayList<>();

    public void delete() {
        this.isDeleted = true;
        this.deletedAt = Instant.now();
    }

    public void removeTemplateVariable(TemplateVariable templateVariable) {
        templateVariable.setEmailTemplate(null);
        this.templateVariableList.remove(templateVariable);
    }

    public void addTemplateVariable(TemplateVariable templateVariable) {
        if (templateVariable.getEmailTemplate() != null) {
            templateVariable.getEmailTemplate().removeTemplateVariable(templateVariable);
        }

        this.templateVariableList.add(templateVariable);
        templateVariable.setEmailTemplate(this);
    }

}
