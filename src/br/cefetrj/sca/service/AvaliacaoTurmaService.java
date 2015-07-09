package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositorio.AvaliacaoTurmaRepositorio;
import br.cefetrj.sca.dominio.repositorio.QuesitoRepositorio;
import br.cefetrj.sca.dominio.repositorio.TurmaRepositorio;
import br.cefetrj.sca.service.util.SolicitaAvaliacaoResponse;
import br.cefetrj.sca.service.util.SolicitaAvaliacaoTurmaResponse;

@Component
public class AvaliacaoTurmaService {

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private TurmaRepositorio turmaRepo;

	@Autowired
	private AvaliacaoTurmaRepositorio avaliacaoRepo;

	@Autowired
	private QuesitoRepositorio quesitoRepo;

	/**
	 * Essa opsis é invocada quando um aluno solicita a avaliação de turmas.
	 * 
	 * @param cpf
	 *            CPF do aluno solicitante.
	 * 
	 * @return
	 */
	public SolicitaAvaliacaoResponse iniciarAvaliacoes(String cpf) {
		getAlunoPorCPF(cpf);

		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
				.getInstance();

		List<Turma> turmas = turmaRepo.getTurmasCursadas(cpf,
				periodoAvaliacao.getSemestreLetivo());

		SolicitaAvaliacaoResponse response = new SolicitaAvaliacaoResponse();
		AvaliacaoTurma turmaAvaliada;

		for (Turma turma : turmas) {

			turmaAvaliada = avaliacaoRepo.getAvaliacaoTurma(turma.getCodigo(),
					cpf);

			response.add(response.new Item(turma.getCodigo(), turma
					.getDisciplina().getNome(), turmaAvaliada != null));
		}

		return response;
	}

	public SolicitaAvaliacaoTurmaResponse solicitaAvaliacaoTurma(String cpf,
			String codigoTurma) {
		Aluno aluno = getAlunoPorCPF(cpf);
		Turma turma = getTurmaPorCodigo(codigoTurma);

		if (!turma.isAlunoInscrito(aluno)) {
			throw new IllegalArgumentException(
					"Erro: aluno não inscrito na turma informada.");
		}

		if (avaliacaoRepo.getAvaliacaoTurma(codigoTurma, cpf) != null) {
			throw new IllegalArgumentException(
					"Erro: turma já avaliada pelo aluno.");
		}

		List<Quesito> quesitos = quesitoRepo.obterTodos();

		SolicitaAvaliacaoTurmaResponse response = new SolicitaAvaliacaoTurmaResponse(
				codigoTurma, turma.getDisciplina().getNome());
		List<String> alternativas;

		for (Quesito quesito : quesitos) {
			alternativas = new ArrayList<String>();

			for (Alternativa alternativa : quesito.getAlternativas()) {
				alternativas.add(alternativa.getDescritor());
			}

			response.add(response.new Item(quesito.getEnunciado(), alternativas));
		}

		return response;
	}

	public void avaliaTurma(String cpf, String codigoTurma,
			List<Integer> respostas, String sugestoes) {

		Aluno aluno = getAlunoPorCPF(cpf);
		Turma turma = getTurmaPorCodigo(codigoTurma);

		if(aluno != null && turma != null){
			if (avaliacaoRepo.getAvaliacaoTurma(codigoTurma, cpf) != null) {
				throw new IllegalArgumentException(
						"Erro: turma já avaliada pelo aluno.");
			}

			List<Quesito> quesitos = quesitoRepo.obterTodos();
			int numRespostas = quesitos.size();

			if (respostas == null || respostas.size() != numRespostas) {
				throw new IllegalArgumentException(
						"Erro: número de respostas inválido. "
								+ "Por favor, forneça respostas para todos os quesitos.");
			}

			List<Alternativa> alternativas = new ArrayList<Alternativa>();
			Quesito quesito;
			Integer resposta;

			for (int i = 0; i < quesitos.size(); ++i) {
				quesito = quesitos.get(i);
				resposta = respostas.get(i);

				if (resposta < 0 || resposta > quesito.getAlternativas().size()) {
					throw new IllegalArgumentException(
							"Erro: código de resposta inválido: " + resposta + ".");
				}

				alternativas.add(quesito.getAlternativas().get(resposta));
			}

			AvaliacaoTurma avaliacao = new AvaliacaoTurma(aluno, turma,
					alternativas);

			if (sugestoes != null && sugestoes.trim().length() > 0) {
				avaliacao.setSugestoes(sugestoes);
			}

			avaliacaoRepo.adicionar(avaliacao);
		}
	}

	private Aluno getAlunoPorCPF(String cpf) {
		if (cpf == null || cpf.trim().equals("")) {
			throw new IllegalArgumentException("CPF deve ser fornecido!");
		}

		Aluno aluno = null;

		try {
			aluno = alunoRepo.getByCPF(cpf);
		} catch (Exception e) {
			throw new IllegalArgumentException("Aluno não encontrado (" + cpf
					+ ")", e);
		}

		return aluno;
	}

	private Turma getTurmaPorCodigo(String codigoTurma) {
		if (codigoTurma == null || codigoTurma.trim().equals("")) {
			throw new IllegalArgumentException("Código da turma é inválido.");
		}

		Turma turma;

		try {
			turma = turmaRepo.getByCodigo(codigoTurma);
		} catch (Exception exc) {
			turma = null;
		}

		if (turma == null) {
			throw new IllegalArgumentException("Erro: turma inexistente.");
		}

		return turma;
	}
}
