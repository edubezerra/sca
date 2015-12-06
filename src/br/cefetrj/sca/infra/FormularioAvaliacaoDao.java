package br.cefetrj.sca.infra;

import br.cefetrj.sca.dominio.FormularioAvaliacao;

public interface FormularioAvaliacaoDao {

	FormularioAvaliacao obterFormulario(String siglaFormulario);
}
