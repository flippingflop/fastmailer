package com.flippingflop.fastmailer.model.vo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

@Getter
@Entity
@NoArgsConstructor
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "email_send_attempt", indexes = {
        @Index(name = "IDX_EMAIL_ID", columnList = "EMAIL_ID")
})
public class EmailSendAttempt {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @CreatedDate
    @Column(name = "CREATED_AT")
    Instant createdAt;

    @Column(name = "EXCEPTION_TRACE", length = 5000)
    String exceptionTrace;

    @JoinColumn(name = "EMAIL_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    Email email;

    public void recordFailure(String exceptionTrace) {
        this.exceptionTrace = exceptionTrace;
    }

}
