package com.flippingflop.fastmailer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class FastmailerApplication {

    public static void main(String[] args) {
        SpringApplication.run(FastmailerApplication.class, args);
    }

}
