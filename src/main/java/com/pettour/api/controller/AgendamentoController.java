package com.pettour.api.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.net.URI;
import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pettour.api.dto.AgendamentoDTO;
import com.pettour.api.dto.AgendamentoDetalhesDTO;
import com.pettour.api.model.Usuario;
import com.pettour.api.service.AgendamentoService;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService agendamentoService;

    @PostMapping
    @Transactional
    public ResponseEntity<AgendamentoDetalhesDTO> agendar(@RequestBody @Valid AgendamentoDTO dados, Authentication authentication, UriComponentsBuilder uriBuilder) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        var agendamento = agendamentoService.agendar(dados, usuarioLogado);
        
        URI uri = uriBuilder.path("/agendamentos/{id}").buildAndExpand(agendamento.getId()).toUri();
        return ResponseEntity.created(uri).body(new AgendamentoDetalhesDTO(agendamento));
    }

    @GetMapping
    public ResponseEntity<List<AgendamentoDetalhesDTO>> listar(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        var lista = agendamentoService.listarPorUsuario(usuarioLogado)
            .stream()
            .map(AgendamentoDetalhesDTO::new)
            .toList();
        
        return ResponseEntity.ok(lista);
    }

    @PatchMapping("/{id}/cancelar")
    @Transactional
    public ResponseEntity<AgendamentoDetalhesDTO> cancelar(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        var agendamento = agendamentoService.cancelar(id, usuarioLogado);

        return ResponseEntity.ok(new AgendamentoDetalhesDTO(agendamento));
    }

    @PatchMapping("/{id}/concluir")
    @Transactional
    public ResponseEntity<AgendamentoDetalhesDTO> concluir(@PathVariable Long id) {
        var agendamento = agendamentoService.concluir(id);
        return ResponseEntity.ok(new AgendamentoDetalhesDTO(agendamento));
    }
    
    @GetMapping("/horarios-disponiveis")
    public ResponseEntity<List<LocalTime>> listarHorariosDisponiveis(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        var horarios = agendamentoService.findHorariosDisponiveis(data);
        return ResponseEntity.ok(horarios);
    }

}