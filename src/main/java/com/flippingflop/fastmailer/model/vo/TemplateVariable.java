package com.flippingflop.fastmailer.model.vo;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Entity
@Table(name = "template_variable", indexes = {
        @Index(name = "IDX_KEY_NAME", columnList = "KEY_NAME")
})
public class TemplateVariable {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "KEY_NAME", nullable = false)
    String keyName;

    @Column(name = "DEFAULT_VALUE", nullable = false)
    String defaultValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMAIL_TEMPLATE_ID", nullable = false)
    EmailTemplate emailTemplate;

    @CreatedDate
    @Column(name = "CREATED_AT")
    Instant createdAt;

}
