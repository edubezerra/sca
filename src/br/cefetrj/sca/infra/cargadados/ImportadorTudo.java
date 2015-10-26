package br.cefetrj.sca.infra.cargadados;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.infra.cargadados.ImportadorInscricoes;
import br.cefetrj.sca.infra.cargadados.ImportadorQuestionarioAvaliacao;

public class ImportadorTudo {
	private static ApplicationContext context;

	public static void main(String[] args) {
		context = new ClassPathXmlApplicationContext(
				new String[] { "applicationContext.xml" });

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		String planilhaMatriculas = "./planilhas/MatriculasAceitas-2015.1.xls";

		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			ImportadorQuestionarioAvaliacao.run();

			ImportadorInscricoes.run(em, planilhaMatriculas);

			ImportadorDocentesTurmasAlocacoes.run();

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
