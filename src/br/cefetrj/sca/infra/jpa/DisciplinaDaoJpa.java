package br.cefetrj.sca.infra.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.infra.DAOException;
import br.cefetrj.sca.infra.DisciplinaDao;

@Component
public class DisciplinaDaoJpa implements DisciplinaDao {
	private GenericDaoJpa<Disciplina> genericDAO = new GenericDaoJpa<Disciplina>();

	public void atualizar(Disciplina disciplina) {
		genericDAO.alterar(disciplina);
	}

	public void excluir(Disciplina disciplina) {
		genericDAO.excluir(Disciplina.class, disciplina.getId());
	}

	public void gravar(Disciplina disciplina) {
		genericDAO.incluir(disciplina);
	}

	public Disciplina obterDisciplina(Long id) throws DAOException {
		return genericDAO.obterPorId(Disciplina.class, id);
	}

	@Override
	public void gravar(List<Disciplina> lista) {
	}

	@Override
	public Disciplina getByNome(String nome) {
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
	public Disciplina getByCodigo(String codigoDisciplina) {
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
}
