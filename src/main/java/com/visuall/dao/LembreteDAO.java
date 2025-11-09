package com.visuall.dao;

import com.visuall.model.LembretePessoal;
import com.visuall.model.dto.LembreteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class LembreteDAO {

    @Inject
    EntityManager em;

    public LembreteResponseDTO readByIdDTO(Integer id) {
        LembretePessoal entity = em.find(LembretePessoal.class, id);
        if (entity == null) return null;
        return toDTO(entity);
    }

    public List<LembreteResponseDTO> readByPacienteId(Integer pacienteId) {
        List<LembretePessoal> lembretes = em.createQuery(
                        "SELECT l FROM LembretePessoal l WHERE l.idPaciente = :pacienteId ORDER BY l.dataCriacao DESC",
                        LembretePessoal.class)
                .setParameter("pacienteId", pacienteId)
                .getResultList();
        return lembretes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public LembretePessoal readById(Integer id) {
        return em.find(LembretePessoal.class, id);
    }

    @Transactional
    public Integer create(LembretePessoal lembrete) {
        em.persist(lembrete);
        em.flush();
        return lembrete.getId();
    }

    @Transactional
    public boolean update(LembretePessoal lembrete) {
        LembretePessoal existing = em.find(LembretePessoal.class, lembrete.getId());
        if (existing == null) return false;
        
        // ✅ Atualiza TODOS os campos
        existing.setTitulo(lembrete.getTitulo());
        existing.setNomeMedico(lembrete.getNomeMedico());
        existing.setEspecialidade(lembrete.getEspecialidade());
        existing.setDataConsulta(lembrete.getDataConsulta());
        existing.setHoraConsulta(lembrete.getHoraConsulta());
        existing.setLocalConsulta(lembrete.getLocalConsulta());
        existing.setObservacoes(lembrete.getObservacoes());
        existing.setAtivo(lembrete.getAtivo());
        existing.setEnviado(lembrete.getEnviado());
        
        em.merge(existing);
        return true;
    }

    @Transactional
    public boolean delete(Integer id, Integer pacienteId) {
        LembretePessoal lembrete = em.find(LembretePessoal.class, id);
        if (lembrete == null || !lembrete.getIdPaciente().equals(pacienteId)) return false;
        em.remove(lembrete);
        return true;
    }

    public List<LembreteResponseDTO> buscarAtivosPorPaciente(Integer pacienteId) {
        List<LembretePessoal> lembretes = em.createQuery(
                        "SELECT l FROM LembretePessoal l WHERE l.idPaciente = :pacienteId AND l.ativo = true ORDER BY l.dataCriacao DESC",
                        LembretePessoal.class)
                .setParameter("pacienteId", pacienteId)
                .getResultList();
        return lembretes.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ✅ Converte entidade para DTO com valores REAIS do banco (não hardcoded)
    private LembreteResponseDTO toDTO(LembretePessoal entity) {
        LembreteResponseDTO dto = new LembreteResponseDTO();
        dto.setId(entity.getId());
        dto.setTitulo(entity.getTitulo());
        
        // ✅ Usa valores reais do banco
        dto.setNomeMedico(entity.getNomeMedico() != null ? entity.getNomeMedico() : "");
        dto.setEspecialidade(entity.getEspecialidade() != null ? entity.getEspecialidade() : "");
        dto.setDataConsulta(entity.getDataConsulta() != null ? entity.getDataConsulta() : LocalDate.now());
        dto.setHoraConsulta(entity.getHoraConsulta());
        dto.setLocalConsulta(entity.getLocalConsulta() != null ? entity.getLocalConsulta() : "");
        dto.setObservacoes(entity.getObservacoes());
        
        // ✅ Corrige lógica de concluído
        dto.setConcluido(!entity.getAtivo());
        dto.setUsuarioId(entity.getIdPaciente());
        
        // ✅ Formata data de criação
        if (entity.getDataCriacao() != null) {
            dto.setDataCriacao(entity.getDataCriacao().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        } else {
            dto.setDataCriacao(LocalDate.now().toString());
        }
        
        return dto;
    }
}
