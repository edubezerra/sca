package br.cefetrj.sca.dominio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.matriculaforaprazo.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.MatriculaForaPrazoRepositorio;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo;

@Component
public class FichaMatriculaForaPrazoFabrica {

	@Autowired
	private DepartamentoRepositorio departamentoRepo;

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	MatriculaForaPrazoRepositorio matriculaForaPrazoRepositorio;

	public FichaMatriculaForaPrazo criar(String matriculaAluno) {
		FichaMatriculaForaPrazo ficha = new FichaMatriculaForaPrazo();

		List<Departamento> departamentos = departamentoRepo.findAll();
		ficha.comDepartamentos(departamentos);

		MatriculaForaPrazo solicitacao = this.getSolicitacaoAtual(matriculaAluno);
		if (solicitacao != null) {
			ficha.comSolicitacoes(solicitacao.getItens());
		}

		Aluno aluno = alunoRepo.findAlunoByMatricula(matriculaAluno);
		ficha.comAluno(aluno);

		return ficha;
	}

	private MatriculaForaPrazo getSolicitacaoAtual(String matriculaAluno) {
		Aluno aluno = alunoRepo.findAlunoByMatricula(matriculaAluno);
		PeriodoLetivo periodoLetivo = PeriodoLetivo.PERIODO_CORRENTE;
		MatriculaForaPrazo solicitacaoAtual = matriculaForaPrazoRepositorio
				.findMatriculaForaPrazoByAlunoAndSemestre(aluno, periodoLetivo);
		return solicitacaoAtual;
	}

}
