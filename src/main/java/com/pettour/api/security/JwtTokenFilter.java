package com.pettour.api.security;

import com.pettour.api.repository.UsuarioRepository;
import com.pettour.api.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("\n[FILTRO JWT] >>> Iniciando filtro para a requisição: " + request.getRequestURI());

        var tokenJWT = recuperarToken(request);

        if (tokenJWT != null) {
            System.out.println("[FILTRO JWT] - Token encontrado no cabeçalho.");
            try {
                var subject = tokenService.getSubject(tokenJWT);
                System.out.println("[FILTRO JWT] - Token válido. Subject (email): " + subject);

                var usuario = usuarioRepository.findByEmail(subject);

                if (usuario != null) {
                    System.out.println("[FILTRO JWT] - Usuário encontrado no banco: " + usuario.getUsername());
                    var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    System.out.println("[FILTRO JWT] - SUCESSO: Usuário autenticado e adicionado ao contexto de segurança.");
                    
                    // --- LINHA DE DIAGNÓSTICO ADICIONADA AQUI ---
                    System.out.println("[FILTRO JWT] - DETALHES AUTENTICAÇÃO: Usuário: " + usuario.getUsername() + " | Papéis: " + usuario.getAuthorities());

                } else {
                    System.out.println("[FILTRO JWT] - ERRO: Usuário com email '" + subject + "' não encontrado no banco de dados.");
                }

            } catch (Exception e) {
                System.out.println("[FILTRO JWT] - ERRO: Token JWT inválido ou expirado! Mensagem: " + e.getMessage());
            }
        } else {
            System.out.println("[FILTRO JWT] - Nenhum token JWT encontrado no cabeçalho 'Authorization'.");
        }

        filterChain.doFilter(request, response);
        System.out.println("[FILTRO JWT] <<< Filtro finalizado.");
    }

    private String recuperarToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }
}