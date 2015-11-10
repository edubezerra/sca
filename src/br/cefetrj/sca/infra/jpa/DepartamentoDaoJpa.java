package br.cefetrj.sca.infra.jpa;

import java.util.List;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.infra.DepartamentoDAO;

@Component
public class DepartamentoDaoJpa implements DepartamentoDAO{
	private GenericDaoJpa<Departamento> genericDAO = new GenericDaoJpa<Departamento>();

	@Override
	public List<Departamento> getTodos() {
		return genericDAO.obterTodos(Departamento.class);
	}

	@Override
	public Departamento getById(Long id) {
		return genericDAO.obterPorId(Departamento.class, id);
	}

}
