package br.cefetrj.sca.service;

import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.CursoRepositorio;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.isencoes.ItemSolicitacaoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas;
import br.cefetrj.sca.dominio.isencoes.SolicitacaoRepositorio;
import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositorio.DisciplinaRepositorio;

@Component
@Scope("session")
public class IsencaoDisciplinasService {

	@Autowired
	CursoRepositorio cursoRepo;

	@Autowired
	private AlunoRepositorio alunoRepo;

	@Autowired
	private DisciplinaRepositorio disciplinaRepo;

	@Autowired
	private SolicitacaoRepositorio solicitacaoRepo;

	SolicitacaoIsencaoDisciplinas solicitacao = null;

	// public boolean autenticaAluno(String matricula, String password) {
	// try {
	// Aluno aluno = alunoRepo.getByMatricula(matricula);
	// if (aluno.getSenha().equals(password))
	// return true;
	// else
	// return false;
	// } catch (NoResultException e) {
	// return false;
	// }
	// }

	public Aluno obterAlunoPorMatricula(String matricula) {
		try {
			return alunoRepo.getByMatricula(matricula);
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Disciplina> obterDisciplinasPorCurso(String numeroVersaoCurso, String siglaCurso) {
		try {
			VersaoCurso versao = cursoRepo.getVersaoCurso(siglaCurso, numeroVersaoCurso);
			return disciplinaRepo.getDisciplinasPorVersaoCurso(versao);
		} catch (NoResultException e) {
			return null;
		}
	}

	public Disciplina obterDisciplinaPorId(long disciplinaId) {
		try {
			return disciplinaRepo.getDisciplinaPorId(disciplinaId);
		} catch (NoResultException e) {
			return null;
		}
	}

	public SolicitacaoIsencaoDisciplinas adicionarSolicitacao(SolicitacaoIsencaoDisciplinas solicitacao) {
		return solicitacaoRepo.adicionarSolicitacao(solicitacao);
	}

	public SolicitacaoIsencaoDisciplinas obterSolicitacaoPorId(long solicitacaoId) {
		try {
			return solicitacaoRepo.obterSolicitacaoPorId(solicitacaoId);
		} catch (NoResultException e) {
			return null;
		}
	}

	public void adicionarItensDeSolicitacao(List<ItemSolicitacaoIsencaoDisciplinas> itensSolicitacao) {
		if (solicitacao == null) {
			solicitacao = new SolicitacaoIsencaoDisciplinas();
		}
		solicitacao.adicionarItens(itensSolicitacao);
	}
}
