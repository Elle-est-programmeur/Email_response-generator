package org.example.email_assistant.Controller;

import lombok.AllArgsConstructor;
import org.example.email_assistant.Service.EmailGeneratorService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;



    @PostMapping("/generate")
    public ResponseEntity<String> emailGenerator(@RequestBody EmailRequest emailRequest) {
        String response = emailGeneratorService.generateResponse(emailRequest);
        return ResponseEntity.ok(response);
    }
}
