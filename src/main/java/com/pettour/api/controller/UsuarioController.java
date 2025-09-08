package com.pettour.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pettour.api.dto.PerfilDTO;
import com.pettour.api.dto.PerfilUpdateDTO;
import com.pettour.api.model.Usuario;
import com.pettour.api.repository.UsuarioRepository;

import jakarta.transaction.Transactional;


@RestController
@RequestMapping("/perfil") // Todas as rotas deste controller começarão com /perfil
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    // Endpoint para VER o perfil do usuário logado
    @GetMapping
    public ResponseEntity<PerfilDTO> verMeuPerfil(Authentication authentication) {
        // O Spring Security nos entrega o objeto 'Authentication' com os dados do usuário logado
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // Converte o Usuario para o nosso DTO de perfil
        PerfilDTO perfil = new PerfilDTO(usuarioLogado);

        return ResponseEntity.ok(perfil);
    }

    // Endpoint para ATUALIZAR o perfil do usuário logado
    @PutMapping
    public ResponseEntity<PerfilDTO> atualizarMeuPerfil(Authentication authentication, @RequestBody PerfilUpdateDTO dados) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        // Atualiza os campos do usuário com os dados recebidos do DTO
        usuarioLogado.setNome(dados.nome());
        usuarioLogado.setTelefone(dados.telefone());
        usuarioLogado.setFotoUrl(dados.fotoUrl());
        usuarioLogado.setLogradouro(dados.logradouro());
        usuarioLogado.setNumero(dados.numero());
        usuarioLogado.setComplemento(dados.complemento());
        usuarioLogado.setBairro(dados.bairro());

        // Salva o usuário atualizado no banco
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioLogado);

        // Retorna o perfil atualizado
        PerfilDTO perfil = new PerfilDTO(usuarioAtualizado);

        return ResponseEntity.ok(perfil);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity<Void> deletarPerfil(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        usuarioRepository.delete(usuarioLogado);
        return ResponseEntity.noContent().build();
    }
}