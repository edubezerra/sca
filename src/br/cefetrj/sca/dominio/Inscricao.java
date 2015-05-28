package br.cefetrj.sca.dominio;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 * Representa a inscrição de um aluno em um turma em um determinado semestre
 * letivo.
 * 
 * @author Eduardo
 * 
 */
@Entity
public class Inscricao {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private Aluno aluno;

	@OneToOne
	private Aproveitamento avaliacao = null;

	/**
	 * Dependência injetada automaticamente.
	 */
	@Transient
	private EstrategiaAvaliacaoAluno estrategiaAvaliacao;

	@SuppressWarnings("unused")
	private Inscricao() {
	}

	public Inscricao(Aluno aluno) {
		super();
		if (aluno == null)
			throw new IllegalArgumentException("aluno não pode ser nulo.");
		this.aluno = aluno;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public BigDecimal getNotaFinal() {
		if (avaliacao == null)
			throw new IllegalStateException(
					"Avaliação ainda não foi registrada.");
		return this.estrategiaAvaliacao.getNotaFinal(this.avaliacao);
	}

	public String getGrau() {
		if (avaliacao == null)
			throw new IllegalStateException(
					"Avaliação ainda não foi registrada.");
		return this.estrategiaAvaliacao.getConceito(this.avaliacao);
	}

	public void setEstrategiaCalculoGrau(EstrategiaAvaliacaoAluno strategia) {
		this.estrategiaAvaliacao = strategia;
	}

	public EnumSituacaoFinalAvaliacao getAvaliacao() {
		if (avaliacao == null)
			throw new IllegalStateException(
					"Avaliação ainda não foi registrada.");
		return this.estrategiaAvaliacao.getSituacaoFinal(this.avaliacao);
	}

	public void registrarAvaliacao(Aproveitamento avaliacao) {
		this.avaliacao = avaliacao;
	}

	public Long getId() {
		return id;
	}
}
