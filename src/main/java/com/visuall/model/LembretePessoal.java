package com.visuall.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "LEMBRETES")
public class LembretePessoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_LEMBRETE")
    private Integer id;

    @Column(name = "MENSAGEM")
    private String titulo;

    @Column(name = "DATA_ENVIO")
    private LocalDateTime dataCriacao;

    @Column(name = "ENVIADO")
    private String enviado;

    @Column(name = "ID_PACIENTE")
    private Integer idPaciente;

    @Column(name = "ID_CONSULTA")
    private Integer idConsulta;

    // ✅ CAMPOS AGORA PERSISTIDOS NO BANCO (removido @Transient)
    @Column(name = "NOME_MEDICO")
    private String nomeMedico;

    @Column(name = "ESPECIALIDADE")
    private String especialidade;

    @Column(name = "DATA_CONSULTA")
    private LocalDate dataConsulta;

    @Column(name = "HORA_CONSULTA")
    private LocalTime horaConsulta;

    @Column(name = "LOCAL_CONSULTA")
    private String localConsulta;

    @Column(name = "OBSERVACOES", length = 1000)
    private String observacoes;

    @Column(name = "ATIVO")
    private Boolean ativo;

    // Getters e Setters
    public Integer getId() { 
        return id; 
    }
    
    public void setId(Integer id) { 
        this.id = id; 
    }

    public String getTitulo() { 
        return titulo; 
    }
    
    public void setTitulo(String titulo) { 
        this.titulo = titulo; 
    }

    public LocalDateTime getDataCriacao() { 
        return dataCriacao; 
    }
    
    public void setDataCriacao(LocalDateTime dataCriacao) { 
        this.dataCriacao = dataCriacao; 
    }

    public String getEnviado() { 
        return enviado; 
    }
    
    public void setEnviado(String enviado) { 
        this.enviado = enviado; 
    }

    public Integer getIdPaciente() { 
        return idPaciente; 
    }
    
    public void setIdPaciente(Integer idPaciente) { 
        this.idPaciente = idPaciente; 
    }

    public Integer getIdConsulta() { 
        return idConsulta; 
    }
    
    public void setIdConsulta(Integer idConsulta) { 
        this.idConsulta = idConsulta; 
    }

    public String getNomeMedico() { 
        return nomeMedico; 
    }
    
    public void setNomeMedico(String nomeMedico) { 
        this.nomeMedico = nomeMedico; 
    }

    public String getEspecialidade() { 
        return especialidade; 
    }
    
    public void setEspecialidade(String especialidade) { 
        this.especialidade = especialidade; 
    }

    public LocalDate getDataConsulta() { 
        return dataConsulta; 
    }
    
    public void setDataConsulta(LocalDate dataConsulta) { 
        this.dataConsulta = dataConsulta; 
    }

    public LocalTime getHoraConsulta() { 
        return horaConsulta; 
    }
    
    public void setHoraConsulta(LocalTime horaConsulta) { 
        this.horaConsulta = horaConsulta; 
    }

    public String getLocalConsulta() { 
        return localConsulta; 
    }
    
    public void setLocalConsulta(String localConsulta) { 
        this.localConsulta = localConsulta; 
    }

    public String getObservacoes() { 
        return observacoes; 
    }
    
    public void setObservacoes(String observacoes) { 
        this.observacoes = observacoes; 
    }

    public Boolean getAtivo() { 
        return ativo; 
    }
    
    public void setAtivo(Boolean ativo) { 
        this.ativo = ativo; 
    }

    @PrePersist
    protected void onCreate() {
        if (dataCriacao == null) {
            dataCriacao = LocalDateTime.now();
        }
        if (enviado == null) {
            enviado = "N";
        }
        if (ativo == null) {
            ativo = true;
        }
    }
}
