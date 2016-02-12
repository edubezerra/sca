package br.cefetrj.sca.infra.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.repositorio.DisciplinaRepositorio;
import br.cefetrj.sca.infra.DAOException;

@Component
public class DisciplinaRepositorioJpa implements DisciplinaRepositorio {
	private GenericDaoJpa<Disciplina> genericDAO = new GenericDaoJpa<Disciplina>();

	public void atualizar(Disciplina disciplina) {
		genericDAO.alterar(disciplina);
	}

	public void excluir(Disciplina disciplina) {
		genericDAO.excluir(Disciplina.class, disciplina.getId());
	}

	public void adicionar(Disciplina disciplina) {
		genericDAO.incluir(disciplina);
	}

	public Disciplina obterDisciplina(Long id) throws DAOException {
		return genericDAO.obterPorId(Disciplina.class, id);
	}

	@Override
	public void adicionarTodas(List<Disciplina> lista) {
	}

	@Override
	public Disciplina getDisciplinaPorNome(String nome) {
		EntityManager entityManager = genericDAO.getEntityManager();
		Query q = entityManager
				.createQuery("from Disciplina d where d.nome = :nomeDisciplinaParam");
		q.setParameter("nomeDisciplinaParam", nome);
		try {
			Disciplina d = (Disciplina) q.getSingleResult();
			return d;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Disciplina> getDisciplinas() {
		return genericDAO.obterTodos(Disciplina.class);
	}

	@Override
	public Disciplina getDisciplinaPorCodigo(String codigoDisciplina) {
		EntityManager entityManager = genericDAO.getEntityManager();
		Query q = entityManager
				.createQuery("from Disciplina d where d.codigo = :codigoDisciplinaParam");
		q.setParameter("codigoDisciplinaParam", codigoDisciplina);
		try {
			Disciplina d = (Disciplina) q.getSingleResult();
			return d;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Disciplina getByCodigo(String codigoDisciplina, String siglaCurso,
			String numeroVersaoCurso) {
		EntityManager entityManager = genericDAO.getEntityManager();
		Query q = entityManager
				.createQuery("from Disciplina d where d.codigo = :codigoDisciplina "
						+ "and d.versaoCurso.numero = :numeroVersaoCurso "
						+ "and d.versaoCurso.curso.sigla = :siglaCurso");
		q.setParameter("codigoDisciplina", codigoDisciplina);
		q.setParameter("siglaCurso", siglaCurso);
		q.setParameter("numeroVersaoCurso", numeroVersaoCurso);
		try {
			Disciplina d = (Disciplina) q.getSingleResult();
			return d;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public Disciplina getByNome(String nomeDisciplina, String siglaCurso,
			String versaoCurso) {
		EntityManager entityManager = genericDAO.getEntityManager();
		Query q = entityManager
				.createQuery("from Disciplina d where d.nome = :nomeDisciplinaParam "
						+ "and d.versaoCurso.numero = :versaoCurso "
						+ "and d.versaoCurso.curso.sigla = :siglaCurso");
		q.setParameter("nomeDisciplinaParam", nomeDisciplina);
		q.setParameter("siglaCurso", siglaCurso);
		q.setParameter("versaoCurso", versaoCurso);
		try {
			Disciplina d = (Disciplina) q.getSingleResult();
			return d;
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Disciplina> getDisciplinasPorVersaoCurso(VersaoCurso versaoCurso) {
		EntityManager entityManager = genericDAO.getEntityManager();
		TypedQuery<Disciplina> q;
		q = entityManager.createQuery(
				"from Disciplina d where d.versaoCurso = :versaoCurso",
				Disciplina.class);
		q.setParameter("versaoCurso", versaoCurso);
		try {
			return q.getResultList();
		} catch (NoResultException e) {
			return new ArrayList<Disciplina>();
		}
	}

	@Override
	public boolean estaContidaEm(Set<Disciplina> preReqs,
			Set<Disciplina> cursadas) {
		return cursadas.containsAll(preReqs);
	}

	@Override
	public Disciplina getDisciplinaPorId(long disciplinaId) {
		return genericDAO.obterPorId(Disciplina.class, disciplinaId);
	}
}
