package com.pettour.api.dto;

import java.time.LocalDateTime;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

public record AgendamentoDTO(
    @NotNull
    Long idServico,

    @NotNull
    Long idPet,

    @NotNull
    @Future // Garante que a data do agendamento seja no futuro
    LocalDateTime data,

    String observacoes
) {
}