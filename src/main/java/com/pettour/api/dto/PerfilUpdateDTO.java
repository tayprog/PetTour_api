package com.pettour.api.dto;

public record PerfilUpdateDTO(
    String nome,
    String telefone,
    String fotoUrl,
    String logradouro,
    String numero,
    String complemento,
    String bairro
) {
}