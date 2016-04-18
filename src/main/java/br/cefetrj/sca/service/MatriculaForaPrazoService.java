package br.cefetrj.sca.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.FichaMatriculaForaPrazoFabrica;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemMatriculaForaPrazo;
import br.cefetrj.sca.dominio.inclusaodisciplina.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.MatriculaForaPrazoRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo.ItemRequerimentoInfo;

@Service
public class MatriculaForaPrazoService {

	public static final int TAMANHO_MAXIMO_COMPROVANTE = 10000000;

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
			throw new IllegalArgumentException("Turma " + codigoTurma + " inválido");
		}

		Turma turma;

		try {
			turma = turmaRepositorio.findTurmaByCodigoAndPeriodoLetivo(codigoTurma, PeriodoLetivo.PERIODO_CORRENTE);
		} catch (Exception exc) {
			turma = null;
		}

		return turma;
	}

	public MatriculaForaPrazo findMatriculaForaPrazoByAlunoAndPeriodo(Long alunoId, PeriodoLetivo periodo) {
		return matriculaForaPrazoRepositorio.findMatriculaForaPrazoByAlunoAndSemestre(alunoId, periodo);
	}

	public List<MatriculaForaPrazo> findMatriculasForaPrazoByAluno(Long idAluno) {
		return matriculaForaPrazoRepositorio.findMatriculasForaPrazoByAluno(idAluno);
	}

	public void incluirItensSolicitacao(List<ItemMatriculaForaPrazo> itensSolicitacao, Aluno aluno,
			PeriodoLetivo semestreLetivo) {

		MatriculaForaPrazo solicitacao = findMatriculaForaPrazoByAlunoAndPeriodo(aluno.getId(), semestreLetivo);
		if (solicitacao != null) {
			solicitacao.addItensSolicitacao(itensSolicitacao);
		} else {
			solicitacao = new MatriculaForaPrazo(itensSolicitacao, aluno, semestreLetivo);
		}

		matriculaForaPrazoRepositorio.save(solicitacao);
	}

	private MatriculaForaPrazo getSolicitacaoAtual(String matriculaAluno) {
		Aluno aluno = findAlunoByMatricula(matriculaAluno);
		Long idAluno = aluno.getId();
		PeriodoLetivo periodoLetivo = PeriodoLetivo.PERIODO_CORRENTE;
		MatriculaForaPrazo solicitacaoAtual = findMatriculaForaPrazoByAlunoAndPeriodo(idAluno, periodoLetivo);
		return solicitacaoAtual;
	}

	public void registrarSolicitacao(FichaMatriculaForaPrazo ficha)
			throws IOException {

		MatriculaForaPrazo matriculaForaPrazo = matriculaForaPrazoRepositorio
				.findMatriculaForaPrazoByAlunoAndSemestre(ficha.getAluno().getId(), PeriodoLetivo.PERIODO_CORRENTE);

		for (ItemRequerimentoInfo item : ficha.getItensRequerimentos()) {
			String codigoTurma = item.getCodigoTurma();
			String codigoDisciplina = item.getCodigoDisciplina();
			Turma turma = turmaRepositorio.findTurmaByCodigoAndDisciplinaAndPeriodo(codigoTurma, codigoDisciplina,
					PeriodoLetivo.PERIODO_CORRENTE);
			Departamento depto = departamentoRepositorio.findDepartamentoByNome(item.getNomeDepartamento());
			int opcao = item.getOpcao();
			matriculaForaPrazo.addItem(turma, depto, opcao);
		}

		matriculaForaPrazo.setComprovante(ficha.getComprovante());

		matriculaForaPrazo.setObservacoes(ficha.getObservacoes());

		matriculaForaPrazoRepositorio.save(matriculaForaPrazo);
	}

	public FichaMatriculaForaPrazo criarFichaSolicitacao(String matricula) {
		return fabrica.criar(matricula);
	}

	public List<Turma> findTurmasByPeriodoLetivo(PeriodoLetivo periodo) {
		return turmaRepositorio.findTurmasAbertasNoPeriodo(periodo);
	}
}
