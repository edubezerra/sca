package br.cefetrj.sca.dominio.atividadecomplementar;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Representa um tipo (categoria) de atividade complementar.
 * 
 * @author Rebecca Salles
 * 
 */
@Entity
public class TipoAtividadeComplementar {

	@Id
	@GeneratedValue
	private Long id;

	/**
	 * Descrição da Atividade Complementar.
	 */
	private String descricao;

	@Enumerated(EnumType.ORDINAL)
	EnumTipoAtividadeComplementar categoria;

	@SuppressWarnings("unused")
	private TipoAtividadeComplementar() {
	}

	public TipoAtividadeComplementar(String descr, EnumTipoAtividadeComplementar categoria) {

		if (descr == null || descr.isEmpty()) {
			throw new IllegalArgumentException("Descrição da atividade complementar é obrigatória.");
		}
		if (categoria == null) {
			throw new IllegalArgumentException("Categoria da atividade complementar é obrigatória.");
		}
		this.descricao = descr;
		this.categoria = categoria;
	}

	public Long getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}

	public EnumTipoAtividadeComplementar getCategoria() {
		return categoria;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((descricao == null) ? 0 : descricao.hashCode());
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
		TipoAtividadeComplementar other = (TipoAtividadeComplementar) obj;
		if (categoria != other.categoria)
			return false;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TipoAtividadeComplementar [descricao=" + descricao + ", categoria=" + categoria + "]";
	}

}
