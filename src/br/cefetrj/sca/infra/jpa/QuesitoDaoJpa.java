package br.cefetrj.sca.infra.jpa;

import java.util.List;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.infra.QuesitoDao;

@Component
public class QuesitoDaoJpa extends GenericDaoJpa<Quesito> implements QuesitoDao {

	@Override
	public List<Quesito> obterTodos() {
		return super.obterTodos(Quesito.class);
	}
}
