package br.cefetrj.sca.dominio;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.FichaAvaliacoes.ItemFicha;

/**
 * Representa a inscrição de um aluno em uma turma.
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
	private NotaFinal avaliacao = null;

	/**
	 * Dependência injetada automaticamente.
	 */
	@Transient
	@Autowired
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

	public Long getId() {
		return id;
	}

	public Aluno getAluno() {
		return aluno;
	}

	public BigDecimal getNotaFinal() {
		if (avaliacao == null)
			return null;
		return this.estrategiaAvaliacao.getNotaFinal(this.avaliacao);
	}

	public String getGrau() {
		if (avaliacao != null) {
			return this.estrategiaAvaliacao.getConceito(this.avaliacao);
		} else {
			return null;
		}
	}

	public void setEstrategiaCalculoGrau(EstrategiaAvaliacaoAluno strategia) {
		this.estrategiaAvaliacao = strategia;
	}

	public EnumSituacaoAvaliacao getAvaliacao() {
		if (avaliacao == null) {
			return EnumSituacaoAvaliacao.INDEFINIDA;
		} else {
			return this.estrategiaAvaliacao.getSituacaoFinal(this.avaliacao);
		}
	}

	public void lancarAvaliacao(ItemFicha item) {
		this.avaliacao = new NotaFinal(item);
	}

	public boolean alunoTemMatricula(String matriculaAluno) {
		return getAluno().getMatricula().equals(matriculaAluno);
	}

}
