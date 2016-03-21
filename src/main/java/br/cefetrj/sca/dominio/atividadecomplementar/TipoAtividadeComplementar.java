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
	 * O código da atividade complementar.
	 */
	private String codigo;
	
	/**
	 * Descrição da Atividade Complementar.
	 */
	private String descricao;
	
	@Enumerated(EnumType.ORDINAL)
	EnumTipoAtividadeComplementar categoria;
	
	@SuppressWarnings("unused")
	private TipoAtividadeComplementar() {
	}

	public TipoAtividadeComplementar(String cod, String descr, EnumTipoAtividadeComplementar categoria) {

		if (cod == null || cod.isEmpty()) {
			throw new IllegalArgumentException("Código da atividade complementar é obrigatório.");
		}
		if (descr == null || descr.isEmpty()) {
			throw new IllegalArgumentException("Descrição da atividade complementar é obrigatória.");
		}
		if (categoria == null) {
			throw new IllegalArgumentException("Categoria da atividade complementar é obrigatória.");
		}
		this.codigo = cod;
		this.descricao = descr;
		this.categoria = categoria;
	}
	
	public Long getId() {
		return id;
	}
	
	public String getCodigo() {
		return codigo;
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
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
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
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
}