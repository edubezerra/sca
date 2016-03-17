package br.cefetrj.sca.dominio.contas;

import javax.persistence.Embeddable;

@Embeddable
public class Email {
	private static ValidadorEmail validador = new ValidadorEmail();

	private String endereco;

	@SuppressWarnings("unused")
	private Email() {
	}

	public Email(String endereco) {
		if (endereco == null || endereco.isEmpty()) {
			throw new IllegalArgumentException(
					"Endereço do email é obrigatório.");
		}
		if (!validador.validar(endereco)) {
			throw new IllegalArgumentException(
					"Endereço fornecido não é válido: " + endereco);
		}
		this.endereco = endereco;
	}

	public String getEndereco() {
		return endereco;
	}

	@Override
	public String toString() {
		return endereco;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((endereco == null) ? 0 : endereco.hashCode());
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
		Email other = (Email) obj;
		if (endereco == null) {
			if (other.endereco != null)
				return false;
		} else if (!endereco.equals(other.endereco))
			return false;
		return true;
	}
}
