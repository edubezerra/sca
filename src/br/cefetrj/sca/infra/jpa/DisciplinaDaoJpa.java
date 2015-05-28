package br.cefetrj.sca.infra.jpa;

import java.util.List;

import javax.el.MethodNotFoundException;
import javax.persistence.EntityManager;
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

	public void excluir(Disciplina disciplina) throws DAOException {
		genericDAO.excluir(Disciplina.class, disciplina.getId());
	}

	public void gravar(Disciplina disciplina) {
		genericDAO.incluir(disciplina);
	}

	public Disciplina obterDisciplina(String nome) {
		EntityManager entityManager = genericDAO.getEntityManager();
		Query q = entityManager.createNamedQuery("Disciplina.obterPorNome");
		q.setParameter("nomeDisciplinaParam", nome);
		Disciplina d = (Disciplina) q.getSingleResult();
		return d;
	}

	public Disciplina obterDisciplina(Long id) throws DAOException {
		return genericDAO.obterPorId(Disciplina.class, id);
	}

	@Override
	public void gravar(List<Disciplina> lista) {
		throw new MethodNotFoundException();
	}

	@Override
	public Disciplina getByNome(String nome) {
		throw new MethodNotFoundException();
	}

	@Override
	public List<Disciplina> getDisciplinas() {
		return genericDAO.obterTodos(Disciplina.class);
	}
}
