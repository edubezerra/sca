package br.cefetrj.sca.dominio.atividadecomplementar;

import java.time.Duration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * Representa uma atividade complementar.
 * 
 * @author Rebecca Salles
 * 
 */
@Entity
public class AtividadeComplementar {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	private TipoAtividadeComplementar tipo;

	/**
	 * Carga horária máxima da atividade complementar.
	 */
	private Duration cargaHorariaMax;

	/**
	 * Carga horária mínima da atividade complementar.
	 */
	private Duration cargaHorariaMin;

	@SuppressWarnings("unused")
	private AtividadeComplementar() {
	}

	public AtividadeComplementar(TipoAtividadeComplementar tipo, Duration chMax, Duration chMin) {

		if (tipo == null) {
			throw new IllegalArgumentException("Tipo da atividade complementar é obrigatório.");
		}
		if (chMax == null || chMax.isZero() || chMax.isNegative()) {
			throw new IllegalArgumentException(
					"Carga horária máxima da atividade complementar é obrigatória e deve ser maior do que zero.");
		}
		if (chMin == null || chMin.isNegative()) {
			throw new IllegalArgumentException(
					"Carga horária mínima da atividade complementar é obrigatória e deve ser maior ou igual a zero.");
		}
		this.tipo = tipo;
		this.cargaHorariaMax = chMax;
		this.cargaHorariaMin = chMin;
	}

	public Long getId() {
		return id;
	}

	public TipoAtividadeComplementar getTipo() {
		return tipo;
	}

	public Duration getCargaHorariaMax() {
		return cargaHorariaMax;
	}

	public Duration getCargaHorariaMin() {
		return cargaHorariaMin;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cargaHorariaMax == null) ? 0 : cargaHorariaMax.hashCode());
		result = prime * result + ((cargaHorariaMin == null) ? 0 : cargaHorariaMin.hashCode());
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		AtividadeComplementar other = (AtividadeComplementar) obj;
		if (cargaHorariaMax == null) {
			if (other.cargaHorariaMax != null)
				return false;
		} else if (!cargaHorariaMax.equals(other.cargaHorariaMax))
			return false;
		if (cargaHorariaMin == null) {
			if (other.cargaHorariaMin != null)
				return false;
		} else if (!cargaHorariaMin.equals(other.cargaHorariaMin))
			return false;
		if (tipo == null) {
			if (other.tipo != null)
				return false;
		} else if (!tipo.equals(other.tipo))
			return false;
		return true;
	}
}
