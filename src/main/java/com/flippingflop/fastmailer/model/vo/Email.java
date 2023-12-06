package com.flippingflop.fastmailer.model.vo;

import com.flippingflop.fastmailer.model.converter.JsonMapConverter;
import com.flippingflop.fastmailer.model.enums.email.EmailStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Entity
@NoArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "email", indexes = {
        @Index(name = "IDX_IS_DELETED", columnList = "IS_DELETED"),
        @Index(name = "IDX_UNIQUE_KEY", columnList = "UNIQUE_KEY", unique = true)
})
public class Email {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "UNIQUE_KEY")
    String uniqueKey;

    @Column(name = "RECIPIENT_EMAIL", nullable = false)
    String recipientEmail;

    @Column(name = "SENDER_EMAIL", nullable = false)
    String senderEmail;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", columnDefinition = "varchar(40)")
    EmailStatus status = EmailStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMAIL_TEMPLATE_ID", nullable = false)
    EmailTemplate emailTemplate;

    @Builder.Default
    @Convert(converter = JsonMapConverter.class)
    @Column(name = "TEMPLATE_DATA", columnDefinition = "JSON")
    Map<String, String> templateData = new HashMap<>();

    @Builder.Default
    @Column(name = "IS_DELETED")
    Boolean isDeleted = false;

    @CreatedDate
    @Column(name = "CREATED_AT")
    Instant createdAt;

    @Column(name = "DELETED_AT")
    Instant deletedAt;

    @PreUpdate
    void preUpdate() {
        if (isDeleted && deletedAt == null) {
            deletedAt = Instant.now();
        }
    }

}
