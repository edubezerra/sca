package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

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

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.MatriculaForaPrazo;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.MatriculaForaPrazoService;
import br.cefetrj.sca.service.util.FichaMatriculaForaPrazo;

@Controller
@RequestMapping("/requerimentoMatricula")
public class MatriculaForaPrazoController {

	protected Logger logger = Logger
			.getLogger(MatriculaForaPrazoController.class.getName());

	@Autowired
	private MatriculaForaPrazoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	/**
	 * Invocado quando o usuário (aluno) solicita a realização de matrículas
	 * fora do prazo.
	 */
	@RequestMapping(value = "/solicitaInclusaoDisciplinas", method = RequestMethod.POST)
	public String solicitaInclusao(Model model, HttpSession sessao) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getLogin();
		try {
			FichaMatriculaForaPrazo ficha = service
					.criarFichaSolicitacao(matricula);

			sessao.setAttribute("fichaMatriculaForaPrazo", ficha);

			model.addAttribute("departamentos", ficha.getDepartamentos());
			model.addAttribute("matricula", ficha.getMatriculaAluno());
			model.addAttribute("aluno", ficha.getAluno());
			model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
			model.addAttribute("turmasDisponiveis", service
					.findTurmasByPeriodoLetivo(PeriodoLetivo.PERIODO_CORRENTE));

			return "/requerimentoMatricula/requerimentoMatriculaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/visualizaRequerimentos", method = RequestMethod.GET)
	public String paginaInicialInclusao(HttpServletRequest request, Model model) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matriculaAluno = usr.getLogin();
			this.carregaHomeView(model, matriculaAluno);
			PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
					.getInstance();
			model.addAttribute("periodoLetivo",
					periodoAvaliacao.getPeriodoLetivo());

			return "/requerimentoMatricula/visualizaRequerimentosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/requerimentoMatricula/requerimentoMatriculaView";
		}
	}

	// @RequestMapping(value = "/incluiSolicitacao", method =
	// RequestMethod.POST)
	// @RequestMapping(value = "/adicionarItemMatriculaForaPrazo", method =
	// RequestMethod.POST)
	// public String adicionarItemMatriculaForaPrazo(Principal principal) {
	// System.out
	// .println("MatriculaForaPrazoController.adicionarItemMatriculaForaPrazo()");
	// System.out.println(principal.getName());
	// return "/requerimentoMatricula/visualizaRequerimentosView";
	// }

	@RequestMapping(value = "/adicionarItemMatriculaForaPrazo", method = RequestMethod.POST)
	public String adicionarItemEmRequerimento(
			@RequestParam String departamento,
			@RequestParam String descritorTurma, @RequestParam int opcao,
			Model model, HttpSession sessao) throws IOException {

		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getLogin();

		try {
			service.adicionarItemRequerimento(descritorTurma, matriculaAluno,
					departamento, opcao);
			model.addAttribute("sucesso", "Turma adicionada ao requerimento.");

			this.carregaHomeView(model, matriculaAluno);

			return "/requerimentoMatricula/requerimentoMatriculaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			FichaMatriculaForaPrazo ficha = (FichaMatriculaForaPrazo) sessao
					.getAttribute("fichaMatriculaForaPrazo");

			model.addAttribute("departamentos", ficha.getDepartamentos());
			model.addAttribute("matricula", ficha.getMatriculaAluno());
			model.addAttribute("aluno", ficha.getAluno());
			model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
			model.addAttribute("itensRequerimento", ficha.getSolicitacoes());

			return "/requerimentoMatricula/requerimentoMatriculaView";
		}
	}

	// public String registrarRequerimentos(@RequestParam MultipartFile file,
	// HttpServletRequest request, Model model) throws IOException {
	//
	// Usuario usr = UsuarioController.getCurrentUser();
	// String matricula = usr.getLogin();
	//
	// try {
	// Map<String, String[]> turmasSolicitadas = request.getParameterMap();
	//
	// service.registrarSolicitacao(turmasSolicitadas, matricula, file,
	// departamento, opcao, observacao);
	// model.addAttribute("sucesso", "Solicitação registrada.");
	// this.carregaHomeView(model, matricula);
	//
	// return "/requerimentoMatricula/visualizaRequerimentosView";
	// } catch (Exception exc) {
	// model.addAttribute("error", exc.getMessage());
	//
	// FichaMatriculaForaPrazo ficha = service
	// .getFichaSolicitacao(matricula);
	//
	// PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
	// .getInstance();
	//
	// model.addAttribute("departamentos", ficha.getDepartamentos());
	// model.addAttribute("cpf", ficha.getCpfAluno());
	// model.addAttribute("aluno", ficha.getAluno());
	// model.addAttribute("periodoLetivo",
	// periodoAvaliacao.getPeriodoLetivo());
	//
	// return "/requerimentoMatricula/requerimentoMatriculaView";
	// }
	//
	// }

	private void carregaHomeView(Model model, String matriculaAluno) {
		MatriculaForaPrazo solicitacaoAtual = service
				.getSolicitacaoAtual(matriculaAluno);
		if (solicitacaoAtual != null) {
			List<MatriculaForaPrazo> solicitacoes = service
					.findMatriculasForaPrazoByAluno(solicitacaoAtual.getAluno()
							.getId());
			List<PeriodoLetivo> listaSemestresLetivos = MatriculaForaPrazo
					.semestresCorrespondentes(solicitacoes);
			model.addAttribute("listaSemestresLetivos", listaSemestresLetivos);
		}

		model.addAttribute("aluno",
				service.findAlunoByMatricula(matriculaAluno));

		// if (solicitacaoAtual != null) {
		// model.addAttribute("numeroSolicitacoes", solicitacaoAtual
		// .getItensSolicitacao().size());
		// } else {
		// model.addAttribute("numeroSolicitacoes", 0);
		// }
	}

	@RequestMapping(value = "/listarSolicitacoes", method = RequestMethod.POST)
	public String listarSolicitacoes(
			@ModelAttribute("login") String matriculaAluno,
			@RequestParam int ano, @RequestParam EnumPeriodo periodo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		try {
			Aluno aluno = service.findAlunoByMatricula(matriculaAluno);
			MatriculaForaPrazo solicitacaoAtual = service
					.findMatriculaForaPrazoByAlunoAndPeriodo(aluno.getId(),
							new PeriodoLetivo(ano, periodo));

			model.addAttribute("solicitacaoAtual", solicitacaoAtual);
			model.addAttribute("aluno", aluno);

			return "/requerimentoMatricula/listaSolicitacoesView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			this.carregaHomeView(model, matriculaAluno);
			return "/requerimentoMatricula/visualizaRequerimentosView";
		}
	}

	// @ModelAttribute("itemMatriculaForaPrazo")
	// MatriculaForaPrazo construirMatriculaForaPrazo() {
	// // return new ItemMatriculaForaPrazo();
	// System.out
	// .println("MatriculaForaPrazoController.construirMatriculaForaPrazo()");
	// return null;
	// }

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute("login") String matricula,
			@RequestParam Long solicitacaoId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Comprovante comprovante = service.getComprovante(solicitacaoId);
			GerenteArquivos.downloadFile(matricula, solicitacaoId, request,
					response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
