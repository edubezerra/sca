package util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;

/**
 * Essa classe realiza a carga de cada item do questionário de avaliação, assim
 * como das alternativas de cada item.
 * 
 * @author Eduardo Bezerra
 *
 */
public class CarregarInformacoesQuestionarioAvaliacao {

	public static void main(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		Alternativa alternativa1 = new Alternativa(
				"Insuficiente(s) ou Ruim(ns)");
		Alternativa alternativa2 = new Alternativa(
				"Suficiente(s) ou Regular(es)");
		Alternativa alternativa3 = new Alternativa("Bom(ns) ou Boa(s)");
		Alternativa alternativa4 = new Alternativa("Ótimo(s) ou Ótima(s)");

		Alternativa[] alternativas = new Alternativa[] { alternativa1,
				alternativa2, alternativa3, alternativa4 };

		for (Alternativa alternativa : alternativas) {
			em.getTransaction().begin();
			em.persist(alternativa);
			em.getTransaction().commit();
		}

		Quesito quest1 = criarQuestao(
				"De forma geral, pode-se dizer que a apresentação do programa"
						+ " e objetivos das disciplinas cursadas ocorre de maneira...",
				alternativas);

		Quesito quest2 = criarQuestao(
				"De forma geral, pode-se dizer que a atualização da bibliografia utilizada "
						+ "e/ou adequação aos tópicos do programa das disciplinas cursadas ocorre de maneira...",
				alternativas);

		Quesito quest3 = criarQuestao(
				"De forma geral, pode-se dizer que o esclarecimento prévio sobre os "
						+ "critérios utilizados para a avaliação dos alunos ocorre de maneira...",
				alternativas);

		Quesito quest4 = criarQuestao(
				"De forma geral, pode-se dizer que o cumprimento do conteúdo "
						+ "programático ocorre de maneira...", alternativas);

		Quesito quest5 = criarQuestao(
				"As práticas pedagógicas promovem a contextualização. "
						+ "De forma geral, pode-se dizer que a relação da teoria com a prática das disciplinas cursadas ocorre de maneira...",
				alternativas);

		Quesito quest6 = criarQuestao(
				"De forma geral, pode-se dizer que o planejamento/organização"
						+ " das aulas pelos professores ocorre de maneira...",
				alternativas);

		Quesito quest7 = criarQuestao(
				"De forma geral, pode-se dizer que a assiduidade dos seus "
						+ "professores ocorre de forma...", alternativas);

		Quesito quest8 = criarQuestao(
				"De forma geral, pode-se dizer que a pontualidade dos "
						+ "seus professores pode ser avaliada como... ",
				alternativas);

		Quesito[] questoes = new Quesito[] { quest1, quest2, quest3, quest4,
				quest5, quest6, quest7, quest8 };

		for (Quesito quesito : questoes) {
			em.getTransaction().begin();
			em.persist(quesito);
			em.getTransaction().commit();
		}

		em.close();
		emf.close();

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private static Quesito criarQuestao(String enunciado,
			Alternativa[] alternativas) {
		Quesito quest = new Quesito(enunciado);

		for (int i = 0; i < alternativas.length; i++) {
			quest.adicionarAlternativa(alternativas[i]);
		}

		return quest;
	}
}
