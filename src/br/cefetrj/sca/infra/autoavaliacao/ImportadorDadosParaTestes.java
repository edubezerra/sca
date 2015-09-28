package br.cefetrj.sca.infra.autoavaliacao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cefetrj.sca.infra.autoavaliacao.ImportadorInscricoes;
import br.cefetrj.sca.infra.autoavaliacao.ImportadorQuestionarioAvaliacao;

public class ImportadorDadosParaTestes {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		String planilhaMatriculas = "./planilhas/MatriculasAceitas-2015.1.xls";
		String planilhaAlocacoesDocentes = "./planilhas/ALOCACAO.DOCENTES.2015.1.xls";

		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			ImportadorQuestionarioAvaliacao.run();

			ImportadorInscricoes.run(em, planilhaMatriculas);

			ImportadorAlocacoesDocentesTurmas.run(em, planilhaAlocacoesDocentes);

			em.getTransaction().commit();

		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		} finally {
			em.close();
			emf.close();
		}

		System.out.println("Importação finalizada!");
	}
}
