package org.example.email_assistant.Controller;

import lombok.Data;

@Data
public class EmailRequest {
    private String emailContent;
    private String tone;
}
