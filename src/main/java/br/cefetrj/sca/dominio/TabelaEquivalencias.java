package br.cefetrj.sca.dominio;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class TabelaEquivalencias {

	@Id
	@GeneratedValue
	Long id;
	
	@OneToMany
	Set<BlocoEquivalencia> blocosEquivalencia;
	
	@ManyToOne
	VersaoCurso versaoCursoCorrespondente;
	
	public TabelaEquivalencias() {
	}
}
