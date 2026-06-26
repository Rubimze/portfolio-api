package com.portfolio.api.controller;

import com.portfolio.api.dto.ContatoRequest;
import com.portfolio.api.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contato")
@CrossOrigin(origins = "*")
public class ContatoController {

    private static final Logger log = LoggerFactory.getLogger(ContatoController.class);
    private final EmailService emailService;

    public ContatoController(EmailService emailService){
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> receberContato(@RequestBody ContatoRequest request) {
        log.info("Processando novo envio de formulário. Remetente: {}", request.email());

        try {
            emailService.enviarEmailContato(request);
            log.info("E-mail processado e enviado com sucesso para a fila do SMTP.");
            return ResponseEntity.ok("Mensagem enviada com sucesso!");
        } catch (Exception e) {
            log.error("Falha ao enviar o e-mail: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Erro ao enviar mensagem.");
        }
    }
}
