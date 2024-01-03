package com.flippingflop.fastmailer.rest;

import com.flippingflop.fastmailer.rest.dto.ApiResponse;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.*;
import com.flippingflop.fastmailer.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/email-template")
public class EmailTemplateController {

    final EmailTemplateService emailTemplateService;

    /**
     * Save email template and it's variables.
     * @param req
     */
    @PostMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<SaveEmailTemplateResponse> saveEmailTemplate(@RequestBody SaveEmailTemplateRequest req) {
        return emailTemplateService.saveEmailTemplate(req);
    }

    /**
     * Load email template by its name
     * @param templateName
     */
    @GetMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<LoadEmailTemplateResponse> loadEmailTemplate(@RequestParam String templateName) {
        return emailTemplateService.loadEmailTemplate(templateName);
    }

    /**
     * Delete email template from SES and database.
     * @param req
     * @return
     */
    @DeleteMapping("")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ApiResponse<DeleteEmailTemplateResponse> deleteEmailTemplate(@RequestBody DeleteEmailTemplateRequest req) {
        return emailTemplateService.deleteEmailTemplate(req);
    }

}
