package br.cefetrj.sca.dominio.repositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.infra.AlternativaDao;

@Component
public class AlternativaRepositorio {
	
	@Autowired
	private AlternativaDao dao;
	
	public Alternativa getById(long id){
		Alternativa a = dao.getById(id);
		return a;
	}
	
}
