package br.cefetrj.sca.dominio.matriculaforaprazo;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.EnumStatusSolicitacao;
import br.cefetrj.sca.dominio.Turma;

@Entity
public class ItemMatriculaForaPrazo {

	@Id
	@GeneratedValue
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dataSolicitacao;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Turma turma;

	@ManyToOne
	@JoinColumn(nullable = false)
	private Departamento departamento;

	@Enumerated(EnumType.ORDINAL)
	private EnumStatusSolicitacao status;

	private int opcao;

	private String observacao;

	@SuppressWarnings("unused")
	private ItemMatriculaForaPrazo() {
	}

	public ItemMatriculaForaPrazo(Turma turma, Departamento departamento, int opcao, Date dataSolicitacao,
			EnumStatusSolicitacao status, String observacao) {

		if (turma == null || departamento == null || status == null || dataSolicitacao == null) {
			throw new IllegalArgumentException("Erro: argumentos inválidos para SolicitacaoInclusao().");
		}

		this.dataSolicitacao = dataSolicitacao;
		this.turma = turma;
		this.departamento = departamento;
		this.status = status;
		this.opcao = opcao;
		this.observacao = observacao;
	}

	public ItemMatriculaForaPrazo(Turma turma, Departamento departamento, int opcao) {

		if (turma == null || departamento == null) {
			throw new IllegalArgumentException("Erro: argumentos inválidos para SolicitacaoInclusao().");
		}

		this.turma = turma;
		this.departamento = departamento;
		this.opcao = opcao;
		this.observacao = "";
		this.dataSolicitacao = new Date();
		this.status = EnumStatusSolicitacao.AGUARDANDO;
	}

	public Turma getTurma() {
		return turma;
	}

	public Long getId() {
		return id;
	}

	public Departamento getDepartamento() {
		return departamento;
	}

	public EnumStatusSolicitacao getStatus() {
		return status;
	}

	public int getOpcao() {
		return opcao;
	}

	public String getObservacao() {
		return observacao;
	}

	public Date getDataSolicitacao() {
		return dataSolicitacao;
	}

	public void setStatus(EnumStatusSolicitacao status) {
		this.status = status;
	}

}
