package br.cefetrj.sca.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.SolicitacaoMatriculaForaPrazoRepositorio;

@Component
public class AnaliseSolicitacoesMatriculaForaPrazoService {

	@Autowired
	private SolicitacaoMatriculaForaPrazoRepositorio solicitacaoRepo;

	@Autowired
	private ProfessorRepositorio professorRepositorio;

	public void homeInclusao(String matricula, Model model) {
		Professor professor = getProfessorByMatricula(matricula);
		List<SolicitacaoMatriculaForaPrazo> solicitacao = solicitacaoRepo
				.findAll();
		if (solicitacao != null) {
			List<PeriodoLetivo> listaSemestresLetivos = SolicitacaoMatriculaForaPrazo
					.semestresCorrespondentes(solicitacao);
			model.addAttribute("listaSemestresLetivos", listaSemestresLetivos);
		}

		model.addAttribute("professor", professor);
	}

	public void listarSolicitacoes(String matricula, EnumPeriodo periodo,
			int ano, Model model) {
		Professor professor = getProfessorByMatricula(matricula);
		Long departamentoId = professor.getDepartmento().getId();
		List<SolicitacaoMatriculaForaPrazo> solicitacoes = solicitacaoRepo
				.getTodasSolicitacoesByDepartamentoSemestre(periodo, ano,
						departamentoId);

		for (SolicitacaoMatriculaForaPrazo solicitacaoInclusao : solicitacoes) {
			Iterator<ItemSolicitacaoMatriculaForaPrazo> it = solicitacaoInclusao
					.getItensSolicitacao().iterator();
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

	public void definirStatusSolicitacao(Long idSolicitacao,
			Long idItemSolicitacao, String status) {
		SolicitacaoMatriculaForaPrazo solicitacao = solicitacaoRepo
				.findOne(idSolicitacao);

		solicitacao.definirStatusItem(idItemSolicitacao, status);

		solicitacaoRepo.save(solicitacao);
	}

	public Comprovante getComprovante(Long solicitacaoId) {
		return solicitacaoRepo.getComprovante(solicitacaoId);
	}

}
