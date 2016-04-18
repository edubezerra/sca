package br.cefetrj.sca.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.EnumStatusSolicitacao;
import br.cefetrj.sca.dominio.FichaMatriculaForaPrazoFabrica;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemMatriculaForaPrazo;
import br.cefetrj.sca.dominio.inclusaodisciplina.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.MatriculaForaPrazoRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo;

@Component
public class MatriculaForaPrazoService {

	private static final int TAMANHO_MAXIMO_COMPROVANTE = 10000000;

	@Autowired
	private DepartamentoRepositorio departamentoRepositorio;

	@Autowired
	private TurmaRepositorio turmaRepositorio;

	@Autowired
	private MatriculaForaPrazoRepositorio matriculaForaPrazoRepositorio;

	@Autowired
	private FichaMatriculaForaPrazoFabrica fabrica;

	@Autowired
	private AlunoRepositorio alunoRepositorio;

	public Aluno findAlunoByMatricula(String matriculaAluno) {
		return alunoRepositorio.findAlunoByMatricula(matriculaAluno);
	}

	public Turma findTurmaByCodigo(String codigoTurma) {
		if (codigoTurma == null || codigoTurma.trim().equals("")) {
			throw new IllegalArgumentException("Turma " + codigoTurma
					+ " inválido");
		}

		Turma turma;

		try {
			turma = turmaRepositorio.findTurmaByCodigoAndPeriodoLetivo(
					codigoTurma, PeriodoLetivo.PERIODO_CORRENTE);
		} catch (Exception exc) {
			turma = null;
		}

		return turma;
	}

	public Departamento findDepartamentoById(String idDepartamento) {
		return departamentoRepositorio.findOne(Long.valueOf(idDepartamento));
	}

	public MatriculaForaPrazo findMatriculaForaPrazoByAlunoAndPeriodo(
			Long alunoId, PeriodoLetivo periodo) {
		return matriculaForaPrazoRepositorio
				.findMatriculaForaPrazoByAlunoAndSemestre(alunoId, periodo);
	}

	public List<MatriculaForaPrazo> findMatriculasForaPrazoByAluno(Long idAluno) {
		return matriculaForaPrazoRepositorio
				.findMatriculasForaPrazoByAluno(idAluno);
	}

	public void incluirItensSolicitacao(
			List<ItemMatriculaForaPrazo> itensSolicitacao, Aluno aluno,
			PeriodoLetivo semestreLetivo) {

		MatriculaForaPrazo solicitacao = findMatriculaForaPrazoByAlunoAndPeriodo(
				aluno.getId(), semestreLetivo);
		if (solicitacao != null) {
			solicitacao.addItensSolicitacao(itensSolicitacao);
		} else {
			solicitacao = new MatriculaForaPrazo(itensSolicitacao, aluno,
					semestreLetivo);
		}

		matriculaForaPrazoRepositorio.save(solicitacao);
	}

	public MatriculaForaPrazo getSolicitacaoAtual(String matriculaAluno) {
		Aluno aluno = findAlunoByMatricula(matriculaAluno);
		Long idAluno = aluno.getId();
		PeriodoLetivo periodoLetivo = PeriodoLetivo.PERIODO_CORRENTE;
		MatriculaForaPrazo solicitacaoAtual = findMatriculaForaPrazoByAlunoAndPeriodo(
				idAluno, periodoLetivo);
		return solicitacaoAtual;
	}

	private void registrarItensSolicitacao(
			Map<String, String[]> turmasSolicitadas, Aluno aluno,
			PeriodoLetivo semestreLetivo, Departamento depto,
			Comprovante comprovante, int opcao, String observacao) {
		int i = 0;
		List<ItemMatriculaForaPrazo> itensSolicitacao = new ArrayList<>();

		while (turmasSolicitadas.containsKey("codigoTurma" + i)) {
			Turma turma = findTurmaByCodigo(turmasSolicitadas.get("codigoTurma"
					+ i)[0]);
			if (turma == null) {
				throw new IllegalArgumentException("Turma "
						+ turmasSolicitadas.get("codigoTurma" + i)[0]
						+ " inexistente");
			} else {

				boolean isSolicitacaoRepetida = isSolicitacaoRepetida(
						aluno.getId(), turma.getCodigo(), semestreLetivo);

				if (isSolicitacaoRepetida) {
					throw new IllegalArgumentException(
							"Erro: Já foi feita uma solicitação para a turma "
									+ turma.getCodigo());
				} else {
					Date dataSolicitacao = new Date();
					itensSolicitacao
							.add(new ItemMatriculaForaPrazo(dataSolicitacao,
									turma, depto, comprovante,
									EnumStatusSolicitacao.AGUARDANDO, opcao,
									observacao));
				}
			}
			++i;
		}

		incluirItensSolicitacao(itensSolicitacao, aluno, semestreLetivo);

	}

	public void registrarSolicitacao(Map<String, String[]> turmasSolicitadas,
			String matriculaAluno, MultipartFile file, String departamento,
			int opcao, String observacao) throws IOException {

		Departamento depto = findDepartamentoById(departamento);
		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
				.getInstance();
		PeriodoLetivo semestreLetivo = periodoAvaliacao.getPeriodoLetivo();
		Aluno aluno = findAlunoByMatricula(matriculaAluno);

		validaComprovante(file);
		Comprovante comprovante = new Comprovante(file.getContentType(),
				file.getBytes(), file.getOriginalFilename());

		registrarItensSolicitacao(turmasSolicitadas, aluno, semestreLetivo,
				depto, comprovante, opcao, observacao);
	}

	private void validaComprovante(MultipartFile file) {
		if (file.getSize() > TAMANHO_MAXIMO_COMPROVANTE) {
			throw new IllegalArgumentException(
					"O arquivo de comprovante deve ter 10mb no máximo");
		}
		String[] tiposAceitos = { "application/pdf", "image/jpeg", "image/png" };
		if (ArrayUtils.indexOf(tiposAceitos, file.getContentType()) < 0) {
			throw new IllegalArgumentException(
					"O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG");
		}
	}

	private boolean isSolicitacaoRepetida(Long idAluno, String codigoTurma,
			PeriodoLetivo semestreLetivo) {
		Turma turma = matriculaForaPrazoRepositorio.getTurmaSolicitada(idAluno,
				codigoTurma, semestreLetivo.getAno(),
				semestreLetivo.getPeriodo());

		if (turma == null) {
			return false;
		} else {
			return true;
		}
	}

	public Comprovante getComprovante(Long solicitacaoId) {
		return matriculaForaPrazoRepositorio.getComprovante(solicitacaoId);
	}

	public FichaMatriculaForaPrazo criarFichaSolicitacao(String matricula) {
		return fabrica.criar(matricula);
	}

	public List<Turma> findTurmasByPeriodoLetivo(PeriodoLetivo periodo) {
		return turmaRepositorio.findTurmasAbertasNoPeriodo(periodo);
	}

	public void adicionarItemRequerimento(String descritorTurma,
			String matricula, String idDepartamento, int opcao) {
		MatriculaForaPrazo requerimento = getSolicitacaoAtual(matricula);
		String componentes[] = descritorTurma.split(";");
		String codigoTurma = componentes[0];
		String codigoDisciplina = componentes[1];
		Turma turma = turmaRepositorio
				.findTurmaByCodigoAndDisciplinaAndPeriodo(codigoTurma,
						codigoDisciplina, PeriodoLetivo.PERIODO_CORRENTE);
		Departamento depto = departamentoRepositorio.findDepartamentoById(Long
				.parseLong(idDepartamento));
		requerimento.addItem(turma, depto, opcao);
		matriculaForaPrazoRepositorio.save(requerimento);
	}
}
