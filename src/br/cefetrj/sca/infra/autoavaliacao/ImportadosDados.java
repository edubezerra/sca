package br.cefetrj.sca.infra.autoavaliacao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cefetrj.sca.infra.autoavaliacao.ImportadorInscricoes;
import br.cefetrj.sca.infra.autoavaliacao.ImportadorQuestionarioAvaliacao;

public class ImportadosDados {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		if (args.length != 1) {
			System.out.println("Forneça o nome da planilha para importação.");
			System.exit(1);
		}
		String arquivoPlanilha = args[0];

		EntityManager em = emf.createEntityManager();

		try {
			em.getTransaction().begin();

			ImportadorQuestionarioAvaliacao.run();

			ImportadorInscricoes.run(em, arquivoPlanilha);

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
