package br.cefetrj.sca.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class LocalAula {

	@GeneratedValue
	@Id
	Long id;

	Integer capacidade;

	private String descricao;

	@SuppressWarnings("unused")
	private LocalAula() {
	}

	public LocalAula(Integer capacidade) {
		if (capacidade == null || capacidade <= 0) {
			throw new IllegalArgumentException(
					"Valor inválido para capacidade do local de aula.");
		}
		this.capacidade = capacidade;
	}

	public LocalAula(String descricao) {
		this.descricao = descricao;
	}

	public Integer getCapacidade() {
		return this.capacidade;
	}

	public Long getId() {
		return this.id;
	}
	
	public String getDescricao() {
		return descricao;
	}
}
