package com.pettour.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pettour.api.model.Pet;
import com.pettour.api.model.Usuario;

public interface PetRepository extends JpaRepository<Pet, Long> {

    //  buscar todos os pets de um usuário específico
    List<Pet> findByUsuario(Usuario usuario);
}