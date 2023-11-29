package com.flippingflop.fastmailer.repository;

import com.flippingflop.fastmailer.model.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
