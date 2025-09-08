package com.pettour.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pettour.api.model.Servico;

public interface ServicoRepository extends JpaRepository<Servico, Long> {

}