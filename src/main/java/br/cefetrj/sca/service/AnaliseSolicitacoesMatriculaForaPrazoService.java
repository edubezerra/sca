package br.cefetrj.sca.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemMatriculaForaPrazo;
import br.cefetrj.sca.dominio.inclusaodisciplina.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositories.MatriculaForaPrazoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Service
public class AnaliseSolicitacoesMatriculaForaPrazoService {

	@Autowired
	private MatriculaForaPrazoRepositorio solicitacaoRepo;

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	public void homeInclusao(String matricula, Model model) {
		Professor professor = getProfessorByMatricula(matricula);
		List<MatriculaForaPrazo> solicitacao = solicitacaoRepo.findAll();
		if (solicitacao != null) {
			List<PeriodoLetivo> listaSemestresLetivos = MatriculaForaPrazo.semestresCorrespondentes(solicitacao);
			model.addAttribute("listaSemestresLetivos", listaSemestresLetivos);
		}

		model.addAttribute("professor", professor);
	}

	public void listarSolicitacoes(String matricula, EnumPeriodo periodo, int ano, Model model) {
		Professor professor = getProfessorByMatricula(matricula);
		Long departamentoId = professor.getDepartmento().getId();
		List<MatriculaForaPrazo> solicitacoes = solicitacaoRepo
				.findMatriculasForaPrazoByDepartamentoAndSemestre(periodo, ano, departamentoId);

		for (MatriculaForaPrazo solicitacaoInclusao : solicitacoes) {
			Iterator<ItemMatriculaForaPrazo> it = solicitacaoInclusao.getItensSolicitacao().iterator();
			while (it.hasNext()) {
				if (!it.next().getDepartamento().getId().equals(departamentoId)) {
					it.remove();
				}
			}
		}

		model.addAttribute("professor", professor);
		model.addAttribute("solicitacoes", solicitacoes);
	}

	public Professor getProfessorByMatricula(String matricula) {
		return professorRepositorio.findProfessorByMatricula(matricula);
	}

	public void definirStatusSolicitacao(Long idSolicitacao, Long idItemSolicitacao, String status) {
		MatriculaForaPrazo solicitacao = solicitacaoRepo.findOne(idSolicitacao);

		solicitacao.definirStatusItem(idItemSolicitacao, status);

		solicitacaoRepo.save(solicitacao);
	}

	public MatriculaForaPrazo getMatriculaForaPrazoById(Long solicitacaoId) {
		return solicitacaoRepo.findOne(solicitacaoId);
	}
}
