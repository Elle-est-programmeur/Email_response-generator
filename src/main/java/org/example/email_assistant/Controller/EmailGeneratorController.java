package org.example.email_assistant.Controller;

import lombok.AllArgsConstructor;
import org.example.email_assistant.Service.EmailGeneratorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class EmailGeneratorController {

    private final EmailGeneratorService emailGeneratorService;



    @PostMapping("/generate")
    public ResponseEntity<String> emailGenerator(@RequestBody EmailRequest emailRequest) {
        String response = emailGeneratorService.generateResponse(emailRequest);
        return ResponseEntity.ok(response);
    }
}
