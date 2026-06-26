package com.portfolio.api.dto;

public record ContatoRequest (
        String nome,
        String email,
        String celular,
        String mensagem
) {}
