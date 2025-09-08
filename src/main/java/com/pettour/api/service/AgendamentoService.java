package com.pettour.api.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pettour.api.dto.AgendamentoDTO;
import com.pettour.api.model.Agendamento;
import com.pettour.api.model.StatusAgendamento;
import com.pettour.api.model.Usuario;
import com.pettour.api.repository.AgendamentoRepository;
import com.pettour.api.repository.PetRepository;
import com.pettour.api.repository.ServicoRepository;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private ServicoRepository servicoRepository;
    @Autowired
    private PetRepository petRepository;

    public Agendamento agendar(AgendamentoDTO dados, Usuario usuario) {
        // LÓGICA DE VALIDAÇÃO DE HORÁRIO
        if (agendamentoRepository.existsByData(dados.data())) {
            throw new RuntimeException("Conflito de agendamento: Este horário já está ocupado.");
        }
        
        var servico = servicoRepository.findById(dados.idServico()).orElseThrow(() -> new RuntimeException("Serviço não encontrado!"));
        var pet = petRepository.findById(dados.idPet()).orElseThrow(() -> new RuntimeException("Pet não encontrado!"));

        // VALIDAÇÃO DE SEGURANÇA: Garante que o pet pertence ao usuário logado
        if (!pet.getUsuario().equals(usuario)) {
            throw new SecurityException("Acesso negado: este pet não pertence ao usuário logado.");
        }

        var agendamento = new Agendamento(null, dados.data(), StatusAgendamento.PENDENTE, dados.observacoes(), usuario, servico, pet);

        return agendamentoRepository.save(agendamento);
    }

    public List<Agendamento> listarPorUsuario(Usuario usuario) {
        return agendamentoRepository.findByUsuario(usuario);
    }

    public Agendamento cancelar(Long idAgendamento, Usuario usuario) {
        var agendamento = agendamentoRepository.findById(idAgendamento)
            .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        // VALIDAÇÃO DE SEGURANÇA: Garante que o agendamento pertence ao usuário logado
        if (!agendamento.getUsuario().equals(usuario)) {
            throw new SecurityException("Acesso negado: este agendamento não pertence a você.");
        }

        // Lógica de negócio: Muda o status para CANCELADO
        agendamento.setStatus(StatusAgendamento.CANCELADO);

        return agendamentoRepository.save(agendamento);
    }

    // --- MÉTODO PARA O ADMIN CONCLUIR ---
    public Agendamento concluir(Long idAgendamento) {
        var agendamento = agendamentoRepository.findById(idAgendamento)
            .orElseThrow(() -> new RuntimeException("Agendamento não encontrado!"));

        // Lógica de negócio: Muda o status para CONCLUIDO
        agendamento.setStatus(StatusAgendamento.CONCLUIDO);

        return agendamentoRepository.save(agendamento);
    }

    // --- MÉTODO PARA CALCULAR HORÁRIOS DISPONÍVEIS ---
    public List<LocalTime> findHorariosDisponiveis(LocalDate data) {
        // 1. Define as regras de negócio
        final LocalTime primeiroHorario = LocalTime.of(9, 0);
        final LocalTime ultimoHorario = LocalTime.of(17, 0); // O último agendamento começa às 17h
        final Duration duracaoDoServico = Duration.ofHours(1);

        // 2. Busca os agendamentos já existentes para o dia
        var inicioDoDia = data.atStartOfDay();
        var fimDoDia = data.atTime(LocalTime.MAX);
        var agendamentosNoDia = agendamentoRepository.findByDataBetween(inicioDoDia, fimDoDia);

        // 3. Extrai apenas os horários que já estão ocupados
        var horariosOcupados = agendamentosNoDia.stream()
            .map(a -> a.getData().toLocalTime())
            .toList();

        // 4. Calcula os horários disponíveis
        var horariosDisponiveis = new ArrayList<LocalTime>();
        var horarioAtual = primeiroHorario;
        while (horarioAtual.isBefore(ultimoHorario.plusSeconds(1))) {
            if (!horariosOcupados.contains(horarioAtual)) {
                horariosDisponiveis.add(horarioAtual);
            }
            horarioAtual = horarioAtual.plus(duracaoDoServico);
        }

        return horariosDisponiveis;
    }

}