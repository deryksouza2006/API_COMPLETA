package com.visuall.service;

import com.visuall.dao.LembreteDAO;
import com.visuall.exception.BusinessException;
import com.visuall.model.LembretePessoal;
import com.visuall.model.dto.LembreteRequestDTO;
import com.visuall.model.dto.LembreteResponseDTO;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class LembreteService {

    @Inject
    LembreteDAO lembreteDAO;

    // ✅ Criar lembrete com TODOS os campos
    @Transactional
    public LembreteResponseDTO criarLembrete(LembreteRequestDTO request) {
        // Validações
        if (request.getTitulo() == null || request.getTitulo().trim().isEmpty()) {
            throw new BusinessException("Título é obrigatório");
        }
        if (request.getNomeMedico() == null || request.getNomeMedico().trim().isEmpty()) {
            throw new BusinessException("Nome do médico é obrigatório");
        }
        if (request.getDataConsulta() == null) {
            throw new BusinessException("Data da consulta é obrigatória");
        }
        if (request.getHoraConsulta() == null) {
            throw new BusinessException("Hora da consulta é obrigatória");
        }

        LembretePessoal lembrete = new LembretePessoal();
        
        // ✅ Define TODOS os campos
        lembrete.setTitulo(request.getTitulo());
        lembrete.setNomeMedico(request.getNomeMedico());
        lembrete.setEspecialidade(request.getEspecialidade());
        lembrete.setDataConsulta(request.getDataConsulta());
        lembrete.setHoraConsulta(request.getHoraConsulta());
        lembrete.setLocalConsulta(request.getLocalConsulta());
        lembrete.setObservacoes(request.getObservacoes());
        lembrete.setIdPaciente(request.getUsuarioId());
        lembrete.setAtivo(true);
        lembrete.setEnviado("N");
        lembrete.setDataCriacao(LocalDateTime.now());

        Integer id = lembreteDAO.create(lembrete);
        if (id == null || id == -1) {
            throw new BusinessException("Erro ao criar lembrete");
        }

        return lembreteDAO.readByIdDTO(id);
    }

    // ✅ Editar lembrete com TODOS os campos
    @Transactional
    public LembreteResponseDTO editarLembrete(Integer id, LembreteRequestDTO request) {
        LembretePessoal existente = lembreteDAO.readById(id);
        if (existente == null) {
            throw new BusinessException("Lembrete não encontrado para edição");
        }

        // ✅ Atualiza TODOS os campos
        existente.setTitulo(request.getTitulo());
        existente.setNomeMedico(request.getNomeMedico());
        existente.setEspecialidade(request.getEspecialidade());
        existente.setDataConsulta(request.getDataConsulta());
        existente.setHoraConsulta(request.getHoraConsulta());
        existente.setLocalConsulta(request.getLocalConsulta());
        existente.setObservacoes(request.getObservacoes());

        lembreteDAO.update(existente);

        return lembreteDAO.readByIdDTO(id);
    }

    // ✅ Marcar como concluído
    @Transactional
    public boolean marcarComoConcluido(Integer id) {
        LembretePessoal lembrete = lembreteDAO.readById(id);
        if (lembrete == null) {
            throw new BusinessException("Lembrete não encontrado");
        }

        lembrete.setEnviado("S");
        lembrete.setAtivo(false);
        return lembreteDAO.update(lembrete);
    }

    // ❌ Excluir lembrete
    @Transactional
    public boolean excluirLembrete(Integer id, Integer usuarioId) {
        return lembreteDAO.delete(id, usuarioId);
    }

    // 🔍 Listar lembretes por usuário
    public List<LembreteResponseDTO> listarPorUsuario(Integer usuarioId) {
        return lembreteDAO.readByPacienteId(usuarioId);
    }

    // 🔍 Listar lembretes ativos
    public List<LembreteResponseDTO> listarAtivos(Integer usuarioId) {
        return lembreteDAO.buscarAtivosPorPaciente(usuarioId);
    }
}
