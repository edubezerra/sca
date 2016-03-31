package br.cefetrj.sca.infra.cargadados;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;

public class ImportadorHabilitacoesParaProfessor {

	EntityManager em = ImportadorTudo.entityManager;

	public void run() {

		Professor professor;
		try {
			Query qry = em.createQuery("from Professor a where a.matricula = :matricula");
			qry.setParameter("matricula", "1506449");
			professor = (Professor) qry.getSingleResult();
		} catch (NoResultException e) {
			professor = null;
		}

		Disciplina d;
		d = findDisciplinaByCodigo("GCC1520");
		professor.habilitarPara(d);

		d = findDisciplinaByCodigo("GCC1208");
		professor.habilitarPara(d);
		
		d = findDisciplinaByCodigo("GCC1518");
		professor.habilitarPara(d);

		em.getTransaction().begin();
		em.merge(professor);
		em.getTransaction().commit();

	}

	private Disciplina findDisciplinaByCodigo(String codigo) {
		Disciplina d;
		try {
			Query qry = em.createQuery("from Disciplina a where a.codigo = :codigo");
			qry.setParameter("codigo", codigo);
			d = (Disciplina) qry.getSingleResult();
		} catch (NoResultException e) {
			d = null;
		}
		return d;
	}
}