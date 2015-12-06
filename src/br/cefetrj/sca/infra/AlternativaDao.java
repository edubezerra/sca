package br.cefetrj.sca.infra;

import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;

public interface AlternativaDao {
	Alternativa getById(long id);
}
