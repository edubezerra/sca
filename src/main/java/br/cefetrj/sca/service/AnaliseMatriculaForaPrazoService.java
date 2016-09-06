package br.cefetrj.sca.service;

import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.MatriculaForaPrazoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Service
public class AnaliseMatriculaForaPrazoService {

	@Autowired
	private MatriculaForaPrazoRepositorio solicitacaoRepo;

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Autowired
	DepartamentoRepositorio departamentoRepositorio;

	public SortedMap<PeriodoLetivo, MatriculaForaPrazo> homeInclusao(
			String matriculaProfessor) {
		SortedMap<PeriodoLetivo, MatriculaForaPrazo> mapa = new TreeMap<>();

		Departamento departamento = departamentoRepositorio
				.findDepartamentoByProfessor(matriculaProfessor);

		List<MatriculaForaPrazo> requerimentos = solicitacaoRepo
				.findMatriculasForaPrazoByDepartamentoAndSemestre(
						PeriodoLetivo.PERIODO_CORRENTE, departamento);

		for (MatriculaForaPrazo matriculaForaPrazo : requerimentos) {
			mapa.put(matriculaForaPrazo.getSemestreLetivo(), matriculaForaPrazo);
		}

		return mapa;
	}

	public List<MatriculaForaPrazo> listarSolicitacoes(
			String matriculaProfessor, PeriodoLetivo periodo) {

		Departamento departamento = departamentoRepositorio
				.findDepartamentoByProfessor(matriculaProfessor);

		List<MatriculaForaPrazo> requerimentos = solicitacaoRepo
				.findMatriculasForaPrazoByDepartamentoAndSemestre(periodo,
						departamento);

		return requerimentos;
	}

	public Professor getProfessorByMatricula(String matricula) {
		return professorRepositorio.findProfessorByMatricula(matricula);
	}

	public void definirStatusSolicitacao(Long idSolicitacao,
			Long idItemSolicitacao, String status) {
		MatriculaForaPrazo solicitacao = solicitacaoRepo.findOne(idSolicitacao);

		solicitacao.definirStatusItem(idItemSolicitacao, status);

		solicitacaoRepo.save(solicitacao);
	}

	public MatriculaForaPrazo getMatriculaForaPrazoById(Long solicitacaoId) {
		return solicitacaoRepo.findOne(solicitacaoId);
	}
}
