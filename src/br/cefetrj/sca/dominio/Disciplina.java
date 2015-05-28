package br.cefetrj.sca.dominio;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

@Entity
public class Disciplina {
	@Id
	@GeneratedValue
	private Long id;

	private String nome;
	private String codigo;
	private Integer quantidadeCreditos;

	@ManyToMany
	@JoinTable(name = "DISCIPLINA_PREREQS", joinColumns = { @JoinColumn(name = "GRADE_ID", referencedColumnName = "ID") }, inverseJoinColumns = { @JoinColumn(name = "DISCIPLINA_ID", referencedColumnName = "ID") })
	private Set<Disciplina> preReqs = new HashSet<Disciplina>();

	@SuppressWarnings("unused")
	private Disciplina() {
	}

	public Disciplina(String nome, String codigo, Integer quantidadeCreditos) {
		super();
		this.nome = nome;
		this.codigo = codigo;
		this.quantidadeCreditos = quantidadeCreditos;
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
			throw new IllegalArgumentException(
					"Valor inv�lido para quantidade de cr�ditos.");
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
