package com.pettour.api.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pettour.api.model.Servico;
import com.pettour.api.repository.ServicoRepository;

@RestController
@RequestMapping("/servicos")
public class ServicoController {

    @Autowired
    private ServicoRepository servicoRepository;

    // READ (All) - Listar todos os serviços (PÚBLICO)
    @GetMapping
    public List<Servico> listarTodos() {
        return servicoRepository.findAll();
    }

    // READ (by ID) - Buscar um serviço pelo ID (PÚBLICO)
    @GetMapping("/{id}")
    public ResponseEntity<Servico> buscarPorId(@PathVariable Long id) {
        Optional<Servico> servico = servicoRepository.findById(id);
        if (servico.isPresent()) {
            return ResponseEntity.ok(servico.get());
        }
        return ResponseEntity.notFound().build(); // Retorna 404 Not Found se não encontrar
    }

    // CREATE - Cadastrar um novo serviço (PROTEGIDO)
    @PostMapping
    public Servico cadastrar(@RequestBody Servico dados) {
        return servicoRepository.save(dados);
    }

    // UPDATE - Atualizar um serviço (PROTEGIDO)
    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @RequestBody Servico dadosNovos) {
        return servicoRepository.findById(id)
            .map(servicoExistente -> {
                servicoExistente.setNome(dadosNovos.getNome());
                servicoExistente.setDescricao(dadosNovos.getDescricao());
                servicoExistente.setPreco(dadosNovos.getPreco());
                servicoExistente.setContato(dadosNovos.getContato());
                Servico servicoAtualizado = servicoRepository.save(servicoExistente);
                return ResponseEntity.ok(servicoAtualizado);
            })
            .orElse(ResponseEntity.notFound().build()); // Retorna 404 se não encontrar o serviço para atualizar
    }

    // DELETE - Deletar um serviço (PROTEGIDO)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!servicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Retorna 404 se o serviço não existe
        }
        servicoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Retorna 204 No Content para sucesso na deleção
    }
}