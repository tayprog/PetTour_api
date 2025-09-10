package com.pettour.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pettour.api.dto.LoginRequestDTO;
import com.pettour.api.dto.LoginResponseDTO;
import com.pettour.api.dto.UsuarioDTO;
import com.pettour.api.model.Usuario;
import com.pettour.api.service.TokenService;
import com.pettour.api.service.UsuarioService;

import jakarta.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AutenticacaoController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/registrar")
    @Transactional // Adicionado por ser uma boa prática em operações de escrita
    public ResponseEntity<Usuario> registrar(@Valid @RequestBody UsuarioDTO dados) {
        System.out.println(">>> [API RECEBEU] DTO de Registro: " + dados.toString());

        //construtor que já define a role como ROLE_USER
        // Correção: Acessa os campos do record diretamente (ex: dados.nome())
        Usuario novoUsuario = new Usuario(dados.nome(), 
        dados.email(), 
        dados.senha()
        );

        // Atribui os dados de endereço ao objeto antes de salvar
        novoUsuario.setLogradouro(dados.logradouro());
        novoUsuario.setNumero(dados.numero());
        novoUsuario.setComplemento(dados.complemento());
        novoUsuario.setBairro(dados.bairro());

        Usuario usuarioSalvo = usuarioService.criarUsuario(novoUsuario);
        return ResponseEntity.ok(usuarioSalvo);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.getEmail(), dados.getSenha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var usuario = (Usuario) authentication.getPrincipal();
        var tokenJWT = tokenService.gerarToken(usuario);

        // Cria o novo DTO de resposta com o token e os dados do usuário
        var responseDTO = new LoginResponseDTO(tokenJWT, usuario);

        return ResponseEntity.ok(responseDTO);
    }
}