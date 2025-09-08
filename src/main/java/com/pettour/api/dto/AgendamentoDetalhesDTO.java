package com.pettour.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.pettour.api.model.Agendamento;
import com.pettour.api.model.StatusAgendamento;

public record AgendamentoDetalhesDTO(
    Long id,
    LocalDateTime data,
    StatusAgendamento status,
    String observacoes,
    ServicoResumoDTO servico,
    PetResumoDTO pet
) {
    // Construtor para facilitar a conversão da Entidade para o DTO
    public AgendamentoDetalhesDTO(Agendamento agendamento) {
        this(
            agendamento.getId(),
            agendamento.getData(),
            agendamento.getStatus(),
            agendamento.getObservacoes(),
            new ServicoResumoDTO(agendamento.getServico().getNome(), agendamento.getServico().getPreco()),
            new PetResumoDTO(agendamento.getPet().getNome(), agendamento.getPet().getRaca())
        );
    }
}

// Pequenos records para resumir os dados do serviço e do pet
record ServicoResumoDTO(String nome, BigDecimal preco) {}
record PetResumoDTO(String nome, String raca) {}