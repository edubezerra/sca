package br.cefetrj.sca.dominio;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Entity
public class VersaoGrade {
	@Id
	@GeneratedValue
	Long id;

	String numero;

	private VersaoGrade() {
	}

	public VersaoGrade(String numero) {
		this.numero = numero;
	}

	public Long getId() {
		return id;
	}

	@OneToMany
	@JoinColumn(name = "VERSAO_GRADE_ID", referencedColumnName = "ID")
	List<Disciplina> disciplinas;
}
