package br.cefetrj.sca.dominio.atividadecomplementar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import br.cefetrj.sca.dominio.atividadecomplementar.AtividadeComplementar;

/**
 * Representa uma tabela de atividades complementares de uma versão de grade curricular de um curso.
 * 
 * @author Rebecca Salles
 * 
 */
@Entity
public final class TabelaAtividadesComplementares {

	@Id
	@GeneratedValue
	Long id;
	
	/**
	 * Atividades Complementares contidas nesta tabela.
	 */
	@OneToMany(cascade = CascadeType.ALL)
	// CascadeType.ALL necessário para persistir Tabelas que registram novas Atividades Complementares
	private List<AtividadeComplementar> atividades = new ArrayList<>();
	
	
	public TabelaAtividadesComplementares() {
	}

	public Long getId() {
		return id;
	}
	
	/**
	 * Retorna todas as atividades complementares desta tabela.
	 */
	public List<AtividadeComplementar> getAtividades() {
		return Collections.unmodifiableList(this.atividades);
	}
	
	/**
	 * Retorna a atividade complementar especificada pelo id passado como parâmetro.
	 */
	public AtividadeComplementar getAtividade(Long IdAtiv) {
		for (AtividadeComplementar ativ : atividades) {
			if (ativ.getId().equals(IdAtiv)) {
				return ativ;
			}
		}
		return null;
	}
	
	/**
	 * Adiciona uma atividade complementar nesta tabela.
	 */
	public void adicionarAtividade(AtividadeComplementar ativ) {
		for (AtividadeComplementar umaAtividade : atividades) {
			if (umaAtividade.getTipo().equals(ativ.getTipo())) {
				throw new IllegalArgumentException(
						"Atividade complementar de tipo duplicado.");
			}
		}
		this.atividades.add(ativ);
	}
	
	public int getQtdAtividades() {
		return this.atividades.size();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		TabelaAtividadesComplementares other = (TabelaAtividadesComplementares) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
