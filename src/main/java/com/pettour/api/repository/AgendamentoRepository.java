package com.pettour.api.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.pettour.api.model.Agendamento;
import com.pettour.api.model.Usuario;
import java.time.LocalDateTime;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {

    // Método para buscar todos os agendamentos de um usuário específico
    List<Agendamento> findByUsuario(Usuario usuario);
    
    //verifica se ja existe um agendamento na mesma data e hora
    boolean existsByData(LocalDateTime data);
   
    //NOVO MÉTODO PARA BUSCAR AGENDAMENTOS POR PERÍODO
    List<Agendamento> findByDataBetween(LocalDateTime inicioDoDia, LocalDateTime fimDoDia);

}