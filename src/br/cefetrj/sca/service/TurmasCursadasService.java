package br.cefetrj.sca.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.EnumSituacaoAvaliacao;
import br.cefetrj.sca.dominio.Inscricao;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.TurmaRepositorio;
import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositorio.DisciplinaRepositorio;

@Deprecated
public class TurmasCursadasService {

	@Autowired
	private AlunoRepositorio ra;

	@Autowired
	private TurmaRepositorio rt;

	@Autowired
	private DisciplinaRepositorio dr;

	/**
	 * 
	 * @param aluno
	 *            para o qual se deseja conhecer as turmar possíveis para ele se
	 *            inscrever.
	 * 
	 * @return coleção de turmas cujas disciplinas da grade curricular estão
	 *         disponíveis para o aluno cursar.
	 */
//	public Set<Turma> getTurmasPossiveis(Aluno aluno) {
//
//		Set<Turma> possiveis = new HashSet<Turma>();
//
//		Set<Disciplina> cursadas = getDisciplinasCursadasComAprovacao(aluno);
//		Set<Disciplina> indisponiveis = getDisciplinasIndisponiveis(aluno);
//
//		List<Turma> abertas = rt
//				.getTurmasAbertas(PeriodoLetivo.SEMESTRE_LETIVO_CORRENTE);
//		for (Turma turma : abertas) {
//			Disciplina disciplina = turma.getDisciplina();
//			if (!cursadas.contains(turma.getDisciplina())
//					&& !indisponiveis.contains(disciplina)) {
//				possiveis.add(turma);
//			}
//		}
//		return possiveis;
//	}

	/**
	 * 
	 * @param aluno
	 *            aluno para o qual se deseja conhecer as disciplinas
	 *            indisponíveis.
	 * 
	 * @return coleção de disciplinas da grade curricular que não estão
	 *         disponíveis para o aluno cursar.
	 * 
	 * 
	 */
//	public Set<Disciplina> getDisciplinasIndisponiveis(Aluno aluno) {
//		Set<Disciplina> cursadas = getDisciplinasCursadasComAprovacao(aluno);
//		Set<Disciplina> disciplinas = new HashSet<Disciplina>();
//		List<Disciplina> todas = dr.getDisciplinas();
//		for (Disciplina disciplina : todas) {
//			Set<Disciplina> preReqs = disciplina.getPreRequisitos();
//			if (!dr.estaContidaEm(preReqs, cursadas)) {
//				disciplinas.add(disciplina);
//			}
//		}
//		return disciplinas;
//	}

	/**
	 * Dada a matrícula de um aluno, retorna a lista de turmas que o aluno já
	 * cursou com aprovação.
	 * 
	 * @param matriculaAluno
	 *            matrícula do aluno.
	 * 
	 * @return lista de turmas que o aluno de matrícula
	 *         <code>matriculaAluno</code> cursou com aprovação.
	 */
//	public List<Turma> obterTurmasCursadasComAprovacao(String matriculaAluno) {
//		Aluno aluno = ra.getByMatricula(matriculaAluno);
//		List<Turma> turmas = rt.obterTodos();
//		for (Turma turma : turmas) {
//			Set<Inscricao> inscricoes = turma.getInscricoes();
//			for (Inscricao inscricao : inscricoes) {
//				if (aluno.getMatricula().equals(
//						inscricao.getAluno().getMatricula())) {
//					EnumSituacaoAvaliacao avaliacao = inscricao
//							.getAvaliacao();
//					if (avaliacao != EnumSituacaoAvaliacao.APROVADO) {
//						break;
//					}
//					turmas.add(turma);
//				}
//			}
//		}
//		return turmas;
//	}

	/**
	 * @param aluno
	 *            aluno para o qual se deseja conhecer as disciplinas cursadas
	 *            com aprovação.
	 *            
	 * @return aluno coleção de disciplinas que o aluno já cursou com sucesso.
	 */
//	public Set<Disciplina> getDisciplinasCursadasComAprovacao(Aluno aluno) {
//		List<Turma> turmas = obterTurmasCursadasComAprovacao(aluno
//				.getMatricula());
//		Set<Disciplina> disciplinas = new HashSet<Disciplina>();
//		for (Turma turma : turmas) {
//			if (turma.aprovado(aluno)) {
//				disciplinas.add(turma.getDisciplina());
//			}
//		}
//		return disciplinas;
//	}

}
