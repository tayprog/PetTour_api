package com.pettour.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public record UsuarioDTO(
    @NotBlank(message = "O nome é obrigatório.")
    String nome,

    @NotBlank(message = "O email é obrigatório.")
    @Email(message = "Formato de email inválido.")
    String email,

    @NotBlank(message = "A senha é obrigatória.")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres.") 
    String senha,

    @NotBlank(message = "A rua é obrigatória.")
    String logradouro,

    @NotBlank(message = "O número é obrigatório.")
    String numero,

    String complemento,
    
    @NotBlank(message = "O bairro é obrigatório.")
    String bairro
) {
}