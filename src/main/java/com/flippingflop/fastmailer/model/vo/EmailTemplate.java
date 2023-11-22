package com.flippingflop.fastmailer.model.vo;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
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

    @Column(name = "IS_DELETED")
    Boolean isDeleted = false;

    @Column(name = "DELETED_AT")
    Instant deletedAt;

    @OneToMany(mappedBy = "emailTemplate", cascade = CascadeType.PERSIST)
    List<TemplateVariable> templateVariableList = new ArrayList<>();

    @PreUpdate
    void preUpdate() {
        if (isDeleted && deletedAt == null) {
            deletedAt = Instant.now();
        }
    }

}
