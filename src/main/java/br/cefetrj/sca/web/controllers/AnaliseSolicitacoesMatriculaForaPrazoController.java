package br.cefetrj.sca.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.MatriculaForaPrazo;
import br.cefetrj.sca.service.AnaliseSolicitacoesMatriculaForaPrazoService;

@Controller
@SessionAttributes("login")
@RequestMapping("/professor")
public class AnaliseSolicitacoesMatriculaForaPrazoController {

	@Autowired
	AnaliseSolicitacoesMatriculaForaPrazoService service;

	@RequestMapping(value = "/homeInclusao", method = RequestMethod.GET)
	public String paginaInicialInclusao(HttpServletRequest request,
			HttpSession session, Model model) {
		try {
			String login = (String) session.getAttribute("login");
			service.homeInclusao(login, model);
			return "/professor/homeInclusaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/listarSolicitacoes", method = RequestMethod.POST)
	public String listarSolicitacoes(@ModelAttribute("login") String matricula,
			@RequestParam int ano, @RequestParam EnumPeriodo periodo,
			Model model) {

		try {
			service.listarSolicitacoes(matricula, periodo, ano, model);
			return "/professor/listaSolicitacoesView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/menuPrincipal")
	public String menuPrincipal(HttpSession session, Model model) {
		String matricula = (String) session.getAttribute("login");
		if (matricula != null) {
			return "/professor/menuPrincipalProfessorView";
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
			String matricula = (String) session.getAttribute("login");
			service.definirStatusSolicitacao(idSolicitacao, idItemSolicitacao,
					status);
			service.listarSolicitacoes(matricula, periodo, ano, model);
			model.addAttribute("sucesso", "Status do item foi atualizado!");
			return "/professor/listaSolicitacoesView";
		} catch (Exception e) {
			String erro = "Ocorreu um erro ao atualizar o status do item de soliticação.";
			model.addAttribute("error", erro);
			return "/homeView";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute("login") String matricula,
			@RequestParam Long solicitacaoId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			MatriculaForaPrazo requerimento = service.getMatriculaForaPrazoById(solicitacaoId);
			Comprovante comprovante = requerimento.getComprovante();
			GerenteArquivos.downloadFile(matricula, solicitacaoId, request, response,
					comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
