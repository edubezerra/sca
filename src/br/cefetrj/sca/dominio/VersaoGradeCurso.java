package br.cefetrj.sca.dominio;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public final class VersaoGradeCurso {
	@Id
	@GeneratedValue
	Long id;

	private final String numero;

	private VersaoGradeCurso() {
		numero = null;
	}

	public VersaoGradeCurso(String numero) {
		this.numero = numero;
	}

	public Long getId() {
		return id;
	}
	
	public String getNumero() {
		return numero;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "VersaoGradeCurso [numero=" + numero + "]";
	}
}
