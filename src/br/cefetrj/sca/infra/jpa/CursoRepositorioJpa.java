package br.cefetrj.sca.infra.jpa;

import javax.persistence.NoResultException;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.CursoRepositorio;
import br.cefetrj.sca.dominio.VersaoCurso;

public class CursoRepositorioJpa implements CursoRepositorio {

	GenericDaoJpa<Curso> cursoDao = new GenericDaoJpa<>();
	GenericDaoJpa<VersaoCurso> versaoCursoDao = new GenericDaoJpa<>();

	public Curso getCursoPorSigla(String siglaCurso) {
		try {
			String queryString = "FROM Curso c WHERE c.sigla = ?";
			Object parametros[] = { siglaCurso };
			return cursoDao.obterEntidade(queryString, parametros);
		} catch (NoResultException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public VersaoCurso getVersaoCurso(String siglaCurso,
			String numeroVersao) {
		try {
			String queryString = "FROM VersaoCurso v WHERE v.curso.sigla = ? and v.numero = ?";
			Object parametros[] = { siglaCurso, numeroVersao };
			return versaoCursoDao.obterEntidade(queryString, parametros);
		} catch (NoResultException e) {
			return null;
		}
	}
}
