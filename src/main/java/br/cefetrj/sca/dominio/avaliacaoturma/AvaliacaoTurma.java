package br.cefetrj.sca.dominio.avaliacaoturma;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Turma;

/**
 * Representa a avaliação de uma turma por um aluno que a cursou. Uma avaliação
 * é composta por diversos quesitos. Para cada um deles, o aluno seleciona uma
 * resposta objetiva correspondente.
 * 
 * @see br.cefetrj.sca.dominio.avaliacaoturma.Quesito
 * @see br.cefetrj.sca.dominio.avaliacaoturma.Alternativa
 * 
 * @author Eduardo Bezerra
 * 
 */
@Entity
public class AvaliacaoTurma {
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Aspectos positivos informados pelo aluno.
	 */
	@Lob
	@Column(length = 8192)
	private String aspectosPositivos;

	/**
	 * Aspectos negativos informados pelo aluno.
	 */
	@Lob
	@Column(length = 8192)
	private String aspectosNegativos;

	/**
	 * Alternativas selecionadas pelo aluno para cada um dos quesitos de
	 * avaliação.
	 */
	@ManyToMany(fetch = FetchType.EAGER)
	@OrderColumn
	@JoinTable(name = "RESPOSTA", joinColumns = {
			@JoinColumn(name = "AVALIACAOTURMA_ID", referencedColumnName = "ID", nullable = true) }, inverseJoinColumns = {
					@JoinColumn(name = "ALTERNATIVA_ID", referencedColumnName = "ID", nullable = false) })
	private List<Alternativa> respostas = new ArrayList<Alternativa>();

	/**
	 * A turma avaliada.
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	private Turma turmaAvaliada;

	/**
	 * A aluno avaliador.
	 */
	@ManyToOne
	@JoinColumn(nullable = false)
	private Aluno alunoAvaliador;

	@SuppressWarnings("unused")
	private AvaliacaoTurma() {
	}

	public AvaliacaoTurma(Aluno aluno, Turma turma, List<Alternativa> respostas) {
		if (aluno == null || turma == null || respostas == null) {
			throw new IllegalArgumentException("Erro: argumentos inválidos para AvaliacaoTurma().");
		}

		this.alunoAvaliador = aluno;
		this.turmaAvaliada = turma;
		this.respostas.addAll(respostas);
	}

	public Long getId() {
		return id;
	}

	public List<Alternativa> getRespostas() {
		return respostas;
	}

	public String getAspectosPositivos() {
		return this.aspectosPositivos;
	}

	public void setAspectosPositivos(String texto) {
		this.aspectosPositivos = texto;
	}

	public String getAspectosNegativos() {
		return this.aspectosNegativos;
	}

	public void setAspectosNegativos(String texto) {
		this.aspectosNegativos = texto;
	}
}
