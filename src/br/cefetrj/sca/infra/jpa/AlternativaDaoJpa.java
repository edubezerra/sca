package br.cefetrj.sca.infra.jpa;

import java.util.logging.Level;

import javax.persistence.NoResultException;

import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.infra.AlternativaDao;

public class AlternativaDaoJpa extends GenericDaoJpa<Alternativa> implements AlternativaDao  {

	@Override
	public Alternativa getById(long id) {
		String consulta = "SELECT a from Alternativa a WHERE a.id = ?";
		Object array[] = { id };
		try {
			return super.obterEntidade(consulta, array);
		} catch (NoResultException ex) {
			logger.log(Level.SEVERE, ex.getMessage() + ": " + id);
			return null;
		}
	}
	
}
