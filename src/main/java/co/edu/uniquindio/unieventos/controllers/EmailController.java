package co.edu.uniquindio.unieventos.controllers;

import co.edu.uniquindio.unieventos.dto.email.EmailDTO;
import co.edu.uniquindio.unieventos.exceptions.EmailException;
import co.edu.uniquindio.unieventos.service.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class  EmailController {

    private final EmailService emailService;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarCorreo(@RequestBody EmailDTO emailDTO) {
        try {
            emailService.enviarCorreo(emailDTO);
            return ResponseEntity.ok("Correo enviado exitosamente.");
        } catch (EmailException e) {
            return ResponseEntity.status(500).body("Error al enviar el correo: " + e.getMessage());
        }
    }

}
