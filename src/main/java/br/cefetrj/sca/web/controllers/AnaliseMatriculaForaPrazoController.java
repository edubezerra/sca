package br.cefetrj.sca.web.controllers;

import java.util.List;
import java.util.SortedMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.matriculaforaprazo.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.AnaliseMatriculaForaPrazoService;

@Controller
@RequestMapping("/matriculaForaPrazo/analise")
public class AnaliseMatriculaForaPrazoController {

	@Autowired
	AnaliseMatriculaForaPrazoService service;

	@RequestMapping(value = "/homeInclusao", method = RequestMethod.GET)
	public String paginaInicialInclusao(HttpServletRequest request,
			HttpSession session, Model model) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matricula = usr.getMatricula();
			
			Professor professor = service.getProfessorByMatricula(matricula);

			SortedMap<PeriodoLetivo, MatriculaForaPrazo> mapa = service.homeInclusao(matricula);

			model.addAttribute("professor", professor);
			model.addAttribute("listaSemestresLetivos", mapa.keySet());

			return "/matriculaForaPrazo/analise/homeInclusaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/listarSolicitacoes", method = RequestMethod.POST)
	public String listarSolicitacoes(@RequestParam int ano,
			@RequestParam EnumPeriodo periodo, Model model) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matriculaProfessor = usr.getMatricula();

			PeriodoLetivo periodoLetivo = new PeriodoLetivo(ano, periodo);
			List<MatriculaForaPrazo> requerimentos = service
					.listarSolicitacoes(matriculaProfessor, periodoLetivo);

			Professor professor = service
					.getProfessorByMatricula(matriculaProfessor);

			model.addAttribute("professor", professor);
			model.addAttribute("requerimentos", requerimentos);
			return "/matriculaForaPrazo/analise/listaSolicitacoesView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/menuPrincipal")
	public String menuPrincipal(HttpSession session, Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		if (matricula != null) {
			return "/matriculaForaPrazo/analise/menuPrincipalProfessorView";
		} else {
			return "/homeView";
		}
	}

	@RequestMapping(value = "/defineStatusAluno", method = RequestMethod.POST)
	public String definirStatusSolicitacao(HttpSession session, Model model,
			@RequestParam(value = "status") String status,
			@RequestParam Long idSolicitacao,
			@RequestParam Long idItemSolicitacao, @RequestParam int ano,
			@RequestParam EnumPeriodo periodo) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matriculaProfessor = usr.getMatricula();
			service.definirStatusSolicitacao(idSolicitacao, idItemSolicitacao,
					status);

			PeriodoLetivo periodoLetivo = new PeriodoLetivo(ano, periodo);
			List<MatriculaForaPrazo> requerimentos = service
					.listarSolicitacoes(matriculaProfessor, periodoLetivo);

			Professor professor = service
					.getProfessorByMatricula(matriculaProfessor);

			model.addAttribute("professor", professor);
			model.addAttribute("requerimentos", requerimentos);
			model.addAttribute("sucesso",
					"Status do item de requerimento foi atualizado!");
			return "/matriculaForaPrazo/analise/listaSolicitacoesView";
		} catch (Exception e) {
			String erro = "Ocorreu um erro ao atualizar o status do item de requerimento.";
			model.addAttribute("error", erro);
			return "/homeView";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@RequestParam Long solicitacaoId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matricula = usr.getMatricula();
			MatriculaForaPrazo requerimento = service
					.getMatriculaForaPrazoById(solicitacaoId);
			Comprovante comprovante = requerimento.getComprovante();
			GerenteArquivos.downloadFile(matricula, solicitacaoId, request,
					response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
