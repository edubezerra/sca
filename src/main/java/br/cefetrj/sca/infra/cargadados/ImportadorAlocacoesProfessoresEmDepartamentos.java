package br.cefetrj.sca.infra.cargadados;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;

public class ImportadorAlocacoesProfessoresEmDepartamentos {
	public void run() {

		EntityManager em = ImportadorTudo.entityManager;

		em.getTransaction().begin();
		Professor professor = obterProfessorPorMatricula(em, "1506449");
		Departamento departamento = obterDepartamentoPorSigla(em, "DEPIN");
		departamento.addProfessor(professor);
		em.getTransaction().commit();

		System.out.println("Foram alocados "
				+ departamento.getProfessores().size()
				+ " professor(es) no departamento " + departamento.getNome());
	}

	private Departamento obterDepartamentoPorSigla(EntityManager em,
			String siglaDepartamento) {
		Query query = em
				.createQuery("from Departamento a where a.sigla = :siglaDepartamento");
		query.setParameter("siglaDepartamento", siglaDepartamento);

		try {
			return (Departamento) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	private Professor obterProfessorPorMatricula(EntityManager em,
			String matricula) {
		Query query = em
				.createQuery("from Professor a where a.matricula = :matricula");
		query.setParameter("matricula", matricula);

		try {
			return (Professor) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

}
