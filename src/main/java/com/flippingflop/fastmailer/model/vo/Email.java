package com.flippingflop.fastmailer.model.vo;

import com.flippingflop.fastmailer.model.enums.email.EmailStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.time.Instant;

@Getter
@Entity
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

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    EmailStatus status = EmailStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMAIL_TEMPLATE_ID", nullable = false)
    EmailTemplate emailTemplate;

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
