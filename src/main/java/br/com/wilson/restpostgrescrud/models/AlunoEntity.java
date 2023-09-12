package br.com.wilson.restpostgrescrud.models;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

import br.com.wilson.restpostgrescrud.dto.AlunoEntradaDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "alunov2")
public class AlunoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "aluno_id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "nome", length = 25, nullable = false, unique = true)
    private String nome;

    @Column(name = "sobre_nome", length = 60)
    @NotNull
    private String sobreNome;

    @Column(name = "data_criacao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataCriacao;

    @Column(name = "data_alteracao")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dataAlteracao;
    
    
    
	public AlunoEntity() {
		super();
	}
	
	
	public AlunoEntity(AlunoEntradaDTO alunoEntradaDTO) {
		super();
		this.nome = alunoEntradaDTO.getNome();
		this.sobreNome = alunoEntradaDTO.getSobreNome();
		this.dataCriacao = new Timestamp(System.currentTimeMillis());
	}


	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobreNome() {
		return sobreNome;
	}
	public void setSobreNome(String sobreNome) {
		this.sobreNome = sobreNome;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Date getDataAlteracao() {
		return dataAlteracao;
	}
	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

}
