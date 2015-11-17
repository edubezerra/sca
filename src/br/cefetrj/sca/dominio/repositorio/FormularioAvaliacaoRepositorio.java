package br.cefetrj.sca.dominio.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.FormularioAvaliacao;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.infra.FormularioAvaliacaoDao;

@Component
public class FormularioAvaliacaoRepositorio {

	@Autowired
	private FormularioAvaliacaoDao faDAO;

	private FormularioAvaliacaoRepositorio() {
	}

	public FormularioAvaliacao obterFormulario(String siglaFormulario) {
		return faDAO.obterFormulario(siglaFormulario);
	}

	public List<Quesito> obterQuesitos(String siglaForm) {
		FormularioAvaliacao form = obterFormulario(siglaForm);
		if(form == null){
			return null;
		}
		
		return form.getQuesitos();
	}
	
}
