package br.cefetrj.sca.infra.jpa;

import javax.persistence.NoResultException;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.CursoRepositorio;

public class CursoRepositorioJpa implements CursoRepositorio {

	GenericDaoJpa<Curso> daoInterno = new GenericDaoJpa<>();

	public Curso getPorSigla(String siglaCurso) {
		try {
		String queryString = "FROM Curso c WHERE c.sigla = ?";
		Object parametros[] = { siglaCurso };
		return daoInterno.obterEntidade(queryString, parametros);
		} catch(NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

}
