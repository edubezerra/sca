package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.Departamento;

public interface DepartamentoDAO {
	
	List<Departamento> getTodos();
	
	Departamento getById(Long id);
	
}
