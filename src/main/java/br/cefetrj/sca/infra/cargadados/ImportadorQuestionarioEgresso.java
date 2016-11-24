package br.cefetrj.sca.infra.cargadados;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.PesquisaAvaliacao;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.dominio.repositories.PesquisaAvaliacaoRepositorio;

@Component
public class ImportadorQuestionarioEgresso {

	@Autowired
	PesquisaAvaliacaoRepositorio pesquisaAvaliacaoRepositorio;

	public void run() {

		System.out.println("ImportadorQuestionarioEgresso.main()");

		PesquisaAvaliacao formGrad = new PesquisaAvaliacao("AvaliacaoEgresso",
				"Avaliação do Egresso");

		Quesito q1 = new Quesito("Atualmente o(a) Sr.(a) está:");
		q1.adicionarAlternativa(new Alternativa("Trabalhando (vá para 2)"));
		q1.adicionarAlternativa(new Alternativa(
				"Trabalhando e estudando (vá para 2)"));
		q1.adicionarAlternativa(new Alternativa("Apenas estudando (vá para 13)"));
		q1.adicionarAlternativa(new Alternativa(
				"Não está trabalhando e nem estudando (vá para 13)"));
		q1.adicionarAlternativa(new Alternativa("Outros"));

		Quesito q2 = new Quesito(
				"O(a) Sr.(a) trabalha na área em que se formou?");
		q2.adicionarAlternativa(new Alternativa("Sim"));
		q2.adicionarAlternativa(new Alternativa("Não"));

		Quesito q3 = new Quesito("Qual é o seu VÍNCULO EMPREGATÍCIO?");
		q3.adicionarAlternativa(new Alternativa(
				"Empregado com carteira assinada"));
		q3.adicionarAlternativa(new Alternativa(
				"Empregado sem carteira assinada"));
		q3.adicionarAlternativa(new Alternativa("Servidor público concursado"));
		q3.adicionarAlternativa(new Alternativa(
				"Autônomo/Prestador de serviços"));
		q3.adicionarAlternativa(new Alternativa("Em contrato temporário"));
		q3.adicionarAlternativa(new Alternativa("Estagiário"));
		q3.adicionarAlternativa(new Alternativa(
				"Proprietário de empresa/negócio"));
		q3.adicionarAlternativa(new Alternativa("Outros"));

		Quesito q4 = new Quesito(
				"Porte da instituição onde exerce a atividade profissional:");
		q4.adicionarAlternativa(new Alternativa(
				"empresa individual (Autônomo ou Profissional Liberal);"));
		q4.adicionarAlternativa(new Alternativa("microempresa"));
		q4.adicionarAlternativa(new Alternativa("pequena empresa"));
		q4.adicionarAlternativa(new Alternativa("média empresa"));
		q4.adicionarAlternativa(new Alternativa("grande empresa"));

		Quesito q5 = new Quesito(
				"(a) Sr.(a) já trabalhava antes de iniciar o seu curso?");
		q5.adicionarAlternativa(new Alternativa("Sim"));
		q5.adicionarAlternativa(new Alternativa("Não"));

		Quesito q6 = new Quesito(
				"Qual o principal TIPO DE ATIVIDADE que o(a) Sr.(a) exerce no seu trabalho atual?");
		q6.adicionarAlternativa(new Alternativa("Atividade Técnica"));
		q6.adicionarAlternativa(new Alternativa("Atividade Administrativa"));
		q6.adicionarAlternativa(new Alternativa("Atividade Gerencial"));
		q6.adicionarAlternativa(new Alternativa("Atividade Comercial"));
		q6.adicionarAlternativa(new Alternativa("Outra"));

		Quesito q7 = new Quesito(
				"Qual é a sua especialidade ou área de atuação na sua profissão?");
		// criar a alternativa com a string de resposta

		Quesito q8 = new Quesito(
				"O conhecimento técnico adquirido no CEFET-RJ durante sua formação foi suficiente e está adequado ao seu trabalho ?");
		q8.adicionarAlternativa(new Alternativa("Sim"));
		q8.adicionarAlternativa(new Alternativa("Não"));

		Quesito q9 = new Quesito(
				"Houve a necessidade do aprendizado de outros conhecimentos teóricos não abordados no seu curso ?");
		q9.adicionarAlternativa(new Alternativa("Muitos"));
		q9.adicionarAlternativa(new Alternativa("Alguns"));
		q9.adicionarAlternativa(new Alternativa("Pouco"));
		q9.adicionarAlternativa(new Alternativa("Nenhum"));

		Quesito q10 = new Quesito(
				"Onde você considera que o curso deve ser aprimorado para que houvesse uma melhor performance em sua atividade profissional ?");
		q10.adicionarAlternativa(new Alternativa("Práticas em laboratório"));
		q10.adicionarAlternativa(new Alternativa("Aulas teóricas"));
		q10.adicionarAlternativa(new Alternativa(
				"Articulação teoria x práticas em laboratório"));
		q10.adicionarAlternativa(new Alternativa("Outro. Qual?"));
		// outro: qual? criar a alternativa com a string de resposta

		Quesito q11 = new Quesito("Onde está LOCALIZADO o seu trabalho atual?");
		q11.adicionarAlternativa(new Alternativa(
				"No próprio município onde realizou o curso."));
		q11.adicionarAlternativa(new Alternativa(
				"Com distância de até 50 Km de onde realizou o curso."));
		q11.adicionarAlternativa(new Alternativa(
				"Em município com distância entre 50 e 100 Km de onde realizou o curso."));
		q11.adicionarAlternativa(new Alternativa(
				"Em município com distância entre 100 e 400 Km"));
		q11.adicionarAlternativa(new Alternativa(
				"Em município com distância superior a 400 Km"));

		Quesito q12 = new Quesito(
				"Considerando o salário mínimo federal de R$ 678,00, qual a sua renda mensal em salários mínimos?");
		q12.adicionarAlternativa(new Alternativa(
				"Entre 1 e 5 salários mínimos (até R$ 3.390,00)"));
		q12.adicionarAlternativa(new Alternativa(
				"Entre 5 e 10 salários mínimos (até R$ 6.780,00)"));
		q12.adicionarAlternativa(new Alternativa(
				"Maior que 10 salários mínimos (> R$ 6.780,00)"));

		Quesito q13 = new Quesito(
				"Qual o seu grau de satisfação com a ÁREA PROFISSIONAL em que o(a) Sr.(a) fez o seu curso?");
		q13.adicionarAlternativa(new Alternativa("Muito satisfeito"));
		q13.adicionarAlternativa(new Alternativa("Satisfeito "));
		q13.adicionarAlternativa(new Alternativa("Indiferente"));
		q13.adicionarAlternativa(new Alternativa("Insatisfeito "));
		q13.adicionarAlternativa(new Alternativa("Muito insatisfeito"));
		q13.adicionarAlternativa(new Alternativa("Não sabe / Não opinou"));

		Quesito q14 = new Quesito(
				"Você tem interesse em dar continuidade aos seus estudos ?");
		q14.adicionarAlternativa(new Alternativa("Sim (vá para 15)"));
		q14.adicionarAlternativa(new Alternativa("Não"));

		Quesito q15 = new Quesito("Na mesma área ?");
		q15.adicionarAlternativa(new Alternativa("Sim"));
		q15.adicionarAlternativa(new Alternativa("Não. Qual?"));

		Quesito q16 = new Quesito("Em que modalidade?");
		q16.adicionarAlternativa(new Alternativa(
				"Lato Sensu (especialização, MBA)"));
		q16.adicionarAlternativa(new Alternativa(
				"Stricto Sensu (mestrado, doutorado)"));

		List<Alternativa> todasAlternativas = new ArrayList<Alternativa>();
		todasAlternativas.addAll(q1.getAlternativas());
		todasAlternativas.addAll(q2.getAlternativas());
		todasAlternativas.addAll(q3.getAlternativas());
		todasAlternativas.addAll(q4.getAlternativas());
		todasAlternativas.addAll(q5.getAlternativas());
		todasAlternativas.addAll(q6.getAlternativas());
		todasAlternativas.addAll(q7.getAlternativas());
		todasAlternativas.addAll(q8.getAlternativas());
		todasAlternativas.addAll(q9.getAlternativas());
		todasAlternativas.addAll(q10.getAlternativas());
		todasAlternativas.addAll(q11.getAlternativas());
		todasAlternativas.addAll(q12.getAlternativas());
		todasAlternativas.addAll(q13.getAlternativas());
		todasAlternativas.addAll(q14.getAlternativas());
		todasAlternativas.addAll(q15.getAlternativas());
		todasAlternativas.addAll(q16.getAlternativas());

		Quesito[] questoes = new Quesito[] { q1, q2, q3, q4, q5, q6, q7, q8,
				q9, q10, q11, q12, q13, q14, q15, q16 };

		for (Quesito quesito : questoes) {
			formGrad.adicionarQuesito(quesito);
		}

		pesquisaAvaliacaoRepositorio.save(formGrad);

		System.out
				.println("Questionário de informações sobre egressos importado com sucesso!!");
	}
}
