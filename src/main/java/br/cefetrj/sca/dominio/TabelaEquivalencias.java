package br.cefetrj.sca.dominio;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

public class TabelaEquivalencias {
	@OneToMany(fetch=FetchType.EAGER)
	List<BlocoEquivalencia> blocosEquivalencia;
	
	@ManyToOne
	VersaoCurso versaoCursoCorrespondente;
}
