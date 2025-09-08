package com.pettour.api.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.pettour.api.dto.PetDTO;
import com.pettour.api.model.Pet;
import com.pettour.api.model.Usuario;
import com.pettour.api.repository.PetRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/perfil/pets")
public class PetController {

    @Autowired
    private PetRepository petRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<Pet> cadastrarPet(@RequestBody @Valid PetDTO dados, Authentication authentication, UriComponentsBuilder uriBuilder) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        Pet novoPet = new Pet(null, dados.nome(), dados.raca(), dados.fotoUrl(), usuarioLogado);
        petRepository.save(novoPet);
        URI uri = uriBuilder.path("/perfil/pets/{id}").buildAndExpand(novoPet.getId()).toUri();
        return ResponseEntity.created(uri).body(novoPet);
    }

    @GetMapping
    public ResponseEntity<List<Pet>> listarMeusPets(Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        List<Pet> pets = petRepository.findByUsuario(usuarioLogado);
        return ResponseEntity.ok(pets);
    }

    // --- NOVO MÉTODO PARA ATUALIZAR ---
    @PutMapping("/{id}")
    @Transactional // Garante que a operação ocorra dentro de uma transação
    public ResponseEntity<Pet> atualizarPet(@PathVariable Long id, @RequestBody @Valid PetDTO dados, Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        var petOptional = petRepository.findById(id);

        if (petOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o pet não existe
        }

        var pet = petOptional.get();
        // VALIDAÇÃO DE POSSE: Verifica se o pet pertence ao usuário logado
        if (!pet.getUsuario().equals(usuarioLogado)) {
            return ResponseEntity.status(403).build(); // Retorna 403 Forbidden
        }

        pet.setNome(dados.nome());
        pet.setRaca(dados.raca());
        pet.setFotoUrl(dados.fotoUrl());

        petRepository.save(pet); // O save aqui funciona como um update

        return ResponseEntity.ok(pet);
    }

    // --- NOVO MÉTODO PARA DELETAR ---
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletarPet(@PathVariable Long id, Authentication authentication) {
        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();
        var petOptional = petRepository.findById(id);

        if (petOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var pet = petOptional.get();
        // VALIDAÇÃO DE POSSE: Verifica se o pet pertence ao usuário logado
        if (!pet.getUsuario().equals(usuarioLogado)) {
            return ResponseEntity.status(403).build(); // Retorna 403 Forbidden
        }

        petRepository.deleteById(id);

        return ResponseEntity.noContent().build(); // Retorna 204 No Content
    }
}