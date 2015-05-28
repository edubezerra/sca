package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;

public interface QuesitoDao {

	List<Quesito> obterTodos();

}
