package br.cefetrj.sca.infra.cargadados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.PesquisaAvaliacao;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.dominio.repositories.PesquisaAvaliacaoRepositorio;

/**
 * Essa classe realiza a carga de cada item do questionário de avaliação das
 * atividades de um professor em um turma sob sua responsabilidae, assim como
 * das alternativas de cada item.
 * 
 * @author Eduardo Bezerra
 *
 */
@Component
public class ImportadorPesquisaAvaliacaoProfessor {

	@Autowired
	PesquisaAvaliacaoRepositorio pesquisaAvaliacaoRepositorio;

	public String run() {

		StringBuilder response = new StringBuilder();

		Alternativa alternativa1 = new Alternativa("Insuficiente(s) ou Ruim(ns)");
		Alternativa alternativa2 = new Alternativa("Suficiente(s) ou Regular(es)");
		Alternativa alternativa3 = new Alternativa("Bom(ns) ou Boa(s)");
		Alternativa alternativa4 = new Alternativa("Ótimo(s) ou Ótima(s)");

		Alternativa[] alternativas = new Alternativa[] { alternativa1, alternativa2, alternativa3, alternativa4 };

		Quesito quest1 = criarQuestao("De forma geral, a apresentação do programa"
				+ " e dos objetivos dessa disciplina ocorreu de maneira...", alternativas);

		Quesito quest2 = criarQuestao(
				"De forma geral, a atualização da bibliografia utilizada "
						+ "e/ou adequação aos tópicos do programa dessa disciplina ocorreu de maneira...",
				alternativas);

		Quesito quest3 = criarQuestao("De forma geral, o esclarecimento prévio sobre os "
				+ "critérios utilizados para a avaliação ocorreu de maneira...", alternativas);

		Quesito quest4 = criarQuestao(
				"De forma geral, o cumprimento do conteúdo " + "programático ocorreu de maneira...", alternativas);

		Quesito quest5 = criarQuestao(
				"As práticas pedagógicas promovem a contextualização. "
						+ "De forma geral, a relação da teoria com a prática nessa disciplina ocorreu de maneira...",
				alternativas);

		Quesito quest6 = criarQuestao(
				"De forma geral, o planejamento/organização" + " das aulas pelo professor ocorreu de maneira...",
				alternativas);

		Quesito quest7 = criarQuestao("De forma geral, a assiduidade do " + "professor ocorreu de forma...",
				alternativas);

		Quesito quest8 = criarQuestao("De forma geral, a pontualidade do " + "professor pode ser avaliada como... ",
				alternativas);

		Quesito[] questoes = new Quesito[] { quest1, quest2, quest3, quest4, quest5, quest6, quest7, quest8 };

		PesquisaAvaliacao form = new PesquisaAvaliacao("AvaliacaoDocente", "Avaliação de Docente");
		for (Quesito quesito : questoes) {
			form.adicionarQuesito(quesito);
		}

		pesquisaAvaliacaoRepositorio.save(form);

		response.append("Questionário de avaliação de professores importado com sucesso!");

		return response.toString();
	}

	private static Quesito criarQuestao(String enunciado, Alternativa[] alternativas) {
		Quesito quest = new Quesito(enunciado);

		for (int i = 0; i < alternativas.length; i++) {
			quest.adicionarAlternativa(alternativas[i]);
		}

		return quest;
	}
}
