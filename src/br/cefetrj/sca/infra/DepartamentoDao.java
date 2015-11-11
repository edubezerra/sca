package br.cefetrj.sca.infra;

import java.util.List;

import br.cefetrj.sca.dominio.Departamento;

public interface DepartamentoDao {
	
	List<Departamento> getTodos();
	
	Departamento getById(Long id);
	
}
