package com.flippingflop.fastmailer.rest;

import com.flippingflop.fastmailer.rest.dto.ApiResponse;
import com.flippingflop.fastmailer.rest.dto.emailTemplate.*;
import com.flippingflop.fastmailer.service.EmailTemplateService;
import lombok.RequiredArgsConstructor;
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
    public ApiResponse<SaveEmailTemplateResponse> saveEmailTemplate(@RequestBody SaveEmailTemplateRequest req) {
        return emailTemplateService.saveEmailTemplate(req);
    }

    /**
     * Load email template by its name
     * @param templateName
     */
    @GetMapping("")
    public ApiResponse<LoadEmailTemplateResponse> loadEmailTemplate(@RequestParam String templateName) {
        return emailTemplateService.loadEmailTemplate(templateName);
    }

    /**
     * Modify email template and its variables.
     * @param req
     * @return
     */
    @PutMapping("")
    public ApiResponse<ModifyEmailTemplateResponse> modifyEmailTemplate(@RequestBody ModifyEmailTemplateRequest req) {
        return emailTemplateService.modifyEmailTemplate(req);
    }

}
