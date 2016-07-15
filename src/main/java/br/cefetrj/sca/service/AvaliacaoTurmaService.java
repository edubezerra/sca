package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PesquisaAvaliacao;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;
import br.cefetrj.sca.dominio.avaliacaoturma.AvaliacaoTurma;
import br.cefetrj.sca.dominio.avaliacaoturma.Quesito;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.AvaliacaoTurmaRepositorio;
import br.cefetrj.sca.dominio.repositories.PesquisaAvaliacaoRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;
import br.cefetrj.sca.service.util.SolicitaAvaliacaoResponse;
import br.cefetrj.sca.service.util.SolicitaAvaliacaoTurmaResponse;

@Service
@Transactional
public class AvaliacaoTurmaService {

	@Autowired
	private AlunoRepositorio alunoRepositorio;

	@Autowired
	private TurmaRepositorio turmaRepositorio;

	@Autowired
	private AvaliacaoTurmaRepositorio avaliacaoRepo;

	@Autowired
	private PesquisaAvaliacaoRepositorio formRepo;

	/**
	 * Essa opsis é invocada quando um aluno solicita a avaliação de turmas.
	 * 
	 * @param matriculaAluno
	 *            CPF do aluno solicitante.
	 * 
	 * @return objeto com informações para construir formulário de avaliações.
	 * 
	 */
	public SolicitaAvaliacaoResponse obterTurmasCursadas(String matriculaAluno) {
		getAlunoPorMatricula(matriculaAluno);

		List<Turma> turmas = turmaRepositorio.findTurmasCursadasPorAluno(matriculaAluno);

		SolicitaAvaliacaoResponse response = new SolicitaAvaliacaoResponse();
		AvaliacaoTurma turmaAvaliada;

		for (Turma turma : turmas) {

			turmaAvaliada = avaliacaoRepo.getAvaliacaoTurma(turma.getId(), matriculaAluno);

			response.add(turma, turmaAvaliada != null);
		}

		return response;
	}

	public SolicitaAvaliacaoTurmaResponse solicitaAvaliacaoTurma(String matriculaAluno, Long idTurma) {
		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		Turma turma = getTurmaPorId(idTurma);

		if (!turma.isAlunoInscrito(aluno)) {
			throw new IllegalArgumentException("Erro: aluno não inscrito na turma informada.");
		}

		if (avaliacaoRepo.getAvaliacaoTurma(idTurma, matriculaAluno) != null) {
			throw new IllegalArgumentException("Erro: turma já avaliada pelo aluno.");
		}

		PesquisaAvaliacao form = formRepo.findByDescritor("AvaliacaoDocente");

		if (form == null) {
			throw new RuntimeException(
					"Erro: questionário não encontrado. Entre em contato com o administrador do sistema.");
		}

		List<Quesito> quesitos = form.getQuesitos();
		SolicitaAvaliacaoTurmaResponse response = new SolicitaAvaliacaoTurmaResponse(turma.getCodigo(),
				turma.getNomeDisciplina());
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

	public void avaliaTurma(String matriculaAluno, Long idTurma, List<Integer> respostas, String aspectosPositivos,
			String aspectosNegativos) {

		Aluno aluno = getAlunoPorMatricula(matriculaAluno);
		Turma turma = getTurmaPorId(idTurma);

		if (aluno != null && turma != null) {
			if (avaliacaoRepo.getAvaliacaoTurma(idTurma, matriculaAluno) != null) {
				throw new IllegalArgumentException("Erro: turma já avaliada pelo aluno.");
			}

			PesquisaAvaliacao form = formRepo.findByDescritor("AvaliacaoDocente");
			List<Quesito> quesitos = form.getQuesitos();
			int numRespostas = quesitos.size();

			if (respostas == null || respostas.size() != numRespostas) {
				throw new IllegalArgumentException("Erro: número de respostas inválido. "
						+ "Por favor, forneça respostas para todos os quesitos.");
			}

			List<Alternativa> alternativas = new ArrayList<Alternativa>();
			Quesito quesito;
			Integer resposta;

			for (int i = 0; i < quesitos.size(); ++i) {
				quesito = quesitos.get(i);
				resposta = respostas.get(i);

				if (resposta < 0 || resposta > quesito.getAlternativas().size()) {
					throw new IllegalArgumentException("Erro: código de resposta inválido: " + resposta + ".");
				}

				alternativas.add(quesito.getAlternativas().get(resposta));
			}

			AvaliacaoTurma avaliacao = new AvaliacaoTurma(aluno, turma, alternativas);

			if (aspectosPositivos != null && aspectosPositivos.trim().length() > 0) {
				avaliacao.setAspectosPositivos(aspectosPositivos);
			}

			if (aspectosNegativos != null && aspectosNegativos.trim().length() > 0) {
				avaliacao.setAspectosNegativos(aspectosNegativos);
			}

			avaliacaoRepo.save(avaliacao);
		}
	}

	private Aluno getAlunoPorMatricula(String matriculaAluno) {
		if (matriculaAluno == null || matriculaAluno.trim().equals("")) {
			throw new IllegalArgumentException("Matrícula deve ser fornecida!");
		}

		Aluno aluno = null;

		try {
			aluno = alunoRepositorio.findAlunoByMatricula(matriculaAluno);
		} catch (Exception e) {
			throw new IllegalArgumentException("Aluno não encontrado (" + matriculaAluno + ")", e);
		}

		return aluno;
	}

	private Turma getTurmaPorId(Long idTurma) {
		if (idTurma == null) {
			throw new IllegalArgumentException("Código da turma não fornecido.");
		}

		Turma turma;

		try {
			turma = turmaRepositorio.findOne(idTurma);
		} catch (Exception exc) {
			turma = null;
		}

		if (turma == null) {
			throw new IllegalArgumentException("Erro: turma inexistente.");
		}

		return turma;
	}
}
