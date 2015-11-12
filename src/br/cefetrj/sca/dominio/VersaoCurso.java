package br.cefetrj.sca.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public final class VersaoGradeCurso {
	@Id
	@GeneratedValue
	Long id;

	private final String numero;

	@ManyToOne
	private Curso curso;

	@SuppressWarnings("unused")
	private VersaoGradeCurso() {
		numero = null;
	}

	public VersaoGradeCurso(String numero, Curso curso) {
		this.numero = numero;
		this.curso = curso;
	}

	public Long getId() {
		return id;
	}

	public String getNumero() {
		return numero;
	}

	@Override
	public String toString() {
		return "VersaoGradeCurso [numero=" + numero + "]";
	}

	public Curso getCurso() {
		return curso;
	}

	public void setCurso(Curso curso) {
		this.curso = curso;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((curso == null) ? 0 : curso.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VersaoGradeCurso other = (VersaoGradeCurso) obj;
		if (curso == null) {
			if (other.curso != null)
				return false;
		} else if (!curso.equals(other.curso))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

}
