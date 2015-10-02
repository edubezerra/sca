package br.cefetrj.sca.dominio;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.apache.commons.lang3.StringUtils;

@Entity
public class Disciplina {
	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Nome desta disciplina.
	 */
	private String nome;

	/**
	 * Código identificador desta disciplina.
	 */
	private String codigo;

	/**
	 * Quantidade de créditos desta disciplina.
	 */
	private Integer quantidadeCreditos;

	/**
	 * Carga horária (em horas-aula) desta disciplina durante sua oferta em um
	 * período letivo. Valores comuns desse atributo são 36, 54, 72 e 90.
	 */
	private int cargaHoraria;

	/**
	 * O período ideal para o aluno cursar essa disciplina.
	 */
	private String periodoIdeal;

	/**
	 * Pré-requisitos desta disciplina. Uma disciplina pode ter zero ou mais
	 * pré-requisitos.
	 */
	@ManyToMany
	@JoinTable(name = "DISCIPLINA_PREREQS", joinColumns = {
			@JoinColumn(name = "GRADE_ID", referencedColumnName = "ID") }, inverseJoinColumns = {
					@JoinColumn(name = "DISCIPLINA_ID", referencedColumnName = "ID") })
	private Set<Disciplina> preReqs = new HashSet<Disciplina>();

	@SuppressWarnings("unused")
	private Disciplina() {
	}

	public Disciplina(String nome, String codigo, String quantidadeCreditos) {
		super();
		if (StringUtils.isBlank(nome)) {
			throw new IllegalArgumentException("Valor inválido para nome.");
		}
		this.nome = nome;
		if (StringUtils.isBlank(codigo)) {
			throw new IllegalArgumentException("Valor inválido para código.");
		}
		this.codigo = codigo;
		try {
			this.quantidadeCreditos = Integer.parseInt(quantidadeCreditos);
			if (this.quantidadeCreditos <= 0) {
				throw new IllegalArgumentException("Valor inválido para quantidade de créditos.");
			}
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Valor inválido para quantidade de créditos.");
		}
	}

	public Disciplina(String codigo, String nome, String quantidadeCreditos, String cargaHoraria, String periodoIdeal) {
		this(nome, codigo, quantidadeCreditos);
		try {
			this.cargaHoraria = Integer.parseInt(cargaHoraria);
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Valor inválido para carga horária.");
		}
		this.periodoIdeal = periodoIdeal;
	}

	public String getNome() {
		return nome;
	}

	public String getCodigo() {
		return codigo;
	}

	public Integer getQuantidadeCreditos() {
		return quantidadeCreditos;
	}

	public void setQuantidadeCreditos(Integer quantidadeCreditos) {
		if (this.quantidadeCreditos <= 0) {
			throw new IllegalArgumentException("Valor inv�lido para quantidade de cr�ditos.");
		}
		this.quantidadeCreditos = quantidadeCreditos;
	}

	public Set<Disciplina> getPreRequisitos() {
		return preReqs;
	}

	public Long getId() {
		return id;
	}
}
