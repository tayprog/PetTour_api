package com.pettour.api.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@CrossOrigin
@RestController
public class TestController {

    @GetMapping("/api/teste")
    public String testeEndpoint() {
        // Pega a autenticação do contexto de segurança para confirmar
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();

        System.out.println("\n\n>>> SUCESSO! O CONTROLLER /api/teste FOI ALCANÇADO! <<<");
        System.out.println(">>> Usuário autenticado: " + userEmail + " <<< \n\n");

        return "Acesso ao endpoint protegido PERMITIDO para o usuário: " + userEmail;
    }
}
