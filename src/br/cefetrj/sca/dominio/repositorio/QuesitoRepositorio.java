package br.cefetrj.sca.dominio.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.infra.QuesitoDao;

@Component
public class QuesitoRepositorio {

	@Autowired
	private QuesitoDao quesitoDAO;

	private QuesitoRepositorio() {
	}

	public List<Quesito> obterTodos() {
		return quesitoDAO.obterTodos();
	}
}
