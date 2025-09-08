package com.pettour.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.pettour.api.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    
    // O Spring Data JPA cria a implementação deste método automaticamente
    UserDetails findByEmail(String email);
}