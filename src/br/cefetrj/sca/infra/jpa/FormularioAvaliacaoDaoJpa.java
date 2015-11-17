package br.cefetrj.sca.infra.jpa;

import java.util.List;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.FormularioAvaliacao;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.infra.FormularioAvaliacaoDao;

@Component
public class FormularioAvaliacaoDaoJpa extends GenericDaoJpa<FormularioAvaliacao>implements FormularioAvaliacaoDao {

	public FormularioAvaliacao obterFormulario(String siglaFormulario) {
		String query = "SELECT f FROM FormularioAvaliacao f where f.sigla = ?";
		Object params[] = { siglaFormulario };
		return super.obterEntidade(query, params);
	}
}
