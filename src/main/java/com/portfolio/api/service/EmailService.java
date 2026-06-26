package com.portfolio.api.service;

import com.portfolio.api.dto.ContatoRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender){
        this.mailSender = mailSender;
    }

    public void enviarEmailContato(ContatoRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();


        message.setTo("contatorubimze@gmail.com");
        message.setSubject("Novo Contato do Portfólio: " + request.nome());
        message.setText("""
                Nome: %s
                E-mail: %s
                Celular: %s
                
                Mensagem:
                %s
                """.formatted(request.nome(), request.email(), request.celular(), request.mensagem()));

        mailSender.send(message);
    }
}

