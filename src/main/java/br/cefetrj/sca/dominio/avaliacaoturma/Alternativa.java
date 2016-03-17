package br.cefetrj.sca.dominio.avaliacaoturma;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Alternativa {
	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false)
	private String descritor;

	@SuppressWarnings("unused")
	private Alternativa() {
	}

	public Alternativa(String descritor) {
		this.descritor = descritor;
	}

	public Long getId() {
		return id;
	}

	public String getDescritor() {
		return descritor;
	}
}
