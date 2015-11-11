package br.cefetrj.sca.service;

import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import br.cefetrj.sca.dominio.EnumStatusSolicitacao;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.ItemSolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.repositorio.InclusaoDisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositorio.ProfessorRepositorio;

@Component
public class AnaliseSolicitacoesMatriculaForaPrazoService {
	
	@Autowired
	private InclusaoDisciplinaRepositorio inclusaoRepo;
	
	@Autowired
	private ProfessorRepositorio professorRepositorio;
	
	public void homeInclusao(String matricula, Model model){
		Professor professor = getProfessorByMatricula(matricula);
		List<SolicitacaoMatriculaForaPrazo> solicitacao = inclusaoRepo.getTodasSolicitacoes();
		if(solicitacao != null){
			List<SemestreLetivo> listaSemestresLetivos = SolicitacaoMatriculaForaPrazo.semestresCorrespondentes(solicitacao);
			model.addAttribute("listaSemestresLetivos", listaSemestresLetivos);
		}
		
		model.addAttribute("professor", professor);
	}
	
	public void listarSolicitacoes(String matricula, EnumPeriodo periodo, int ano, Model model){
		Professor professor = getProfessorByMatricula(matricula);
		Long departamentoId = professor.getDepartmento().getId();
		List<SolicitacaoMatriculaForaPrazo> solicitacoes = inclusaoRepo
				.getTodasSolicitacoesByDepartamentoSemestre(periodo, ano,departamentoId);
		
		for (SolicitacaoMatriculaForaPrazo solicitacaoInclusao : solicitacoes) {
			Iterator<ItemSolicitacaoMatriculaForaPrazo> it = solicitacaoInclusao.getItemSolicitacao().iterator();
			while (it.hasNext()) {
			    if (!it.next().getDepartamento().getId().equals(departamentoId)) {
			        it.remove();
			    }
			}
		}
		
		model.addAttribute("professor", professor);
		model.addAttribute("solicitacoes", solicitacoes);
	}
	
	public Professor getProfessorByMatricula(String matricula){
		return professorRepositorio.getProfessor(matricula);
	}
	
	public void atualizaStatusAluno(Long idItemSolicitacao, String status, Model model){
		ItemSolicitacaoMatriculaForaPrazo itemSolicitacao = inclusaoRepo.getItemSolicitacaoById(idItemSolicitacao);
		if(status.equalsIgnoreCase("deferido")){
			itemSolicitacao.setStatus(EnumStatusSolicitacao.DEFERIDO);
		} else if(status.equalsIgnoreCase("indeferido")) { 
			itemSolicitacao.setStatus(EnumStatusSolicitacao.INDEFERIDO);
		}
		
		if(inclusaoRepo.alterarItemSolcitacao(itemSolicitacao)){
			model.addAttribute("sucesso","Aluno "+itemSolicitacao.getStatus()+" com Sucesso!");
		} else {
			throw new IllegalArgumentException("Ocorreu um erro ao atualizar o status do Aluno");
		}
		
	}

	public Comprovante getComprovante(Long solicitacaoId) {
		return inclusaoRepo.getComprovante(solicitacaoId);
	}
	
}
