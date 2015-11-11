package br.cefetrj.sca.dominio.repositorio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.infra.DepartamentoDao;
import br.cefetrj.sca.infra.TurmaDao;

@Component
public class DepartamentoRepositorio {
	
	@Autowired
	private DepartamentoDao departamentoDao;
	
	@Autowired
	private TurmaDao turmaDao;
	
	private DepartamentoRepositorio(){
		
	}
	
	public List<Departamento> getTodos(){
		return departamentoDao.getTodos();
	}
	
	public Departamento getById(Long id){
		return departamentoDao.getById(id);
	}

	public Turma getByCodigo(String codigo) {
		return turmaDao.getByCodigo(codigo);
	}
	
}
