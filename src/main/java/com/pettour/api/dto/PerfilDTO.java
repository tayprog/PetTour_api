package com.pettour.api.dto;


import com.pettour.api.model.Usuario;

public record PerfilDTO(
    Long id,
    String nome,
    String email,
    String telefone,
    String fotoUrl,
    String logradouro,
    String numero,
    String complemento,
    String bairro

) {
    // Construtor adicional para facilitar a conversão de Usuario para PerfilDTO
    public PerfilDTO(Usuario usuario) {
        this(
            usuario.getId(), 
            usuario.getNome(), 
            usuario.getEmail(), 
            usuario.getTelefone(), 
            usuario.getFotoUrl(),
            usuario.getLogradouro(),
            usuario.getNumero(),
            usuario.getComplemento(),
            usuario.getBairro()
        );
    }
}