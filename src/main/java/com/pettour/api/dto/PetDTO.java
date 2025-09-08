package com.pettour.api.dto;

import jakarta.validation.constraints.NotBlank;

public record PetDTO(
    @NotBlank
    String nome,

    @NotBlank
    String raca,

    String fotoUrl
) {
}