package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.SolicitacaoMatriculaForaPrazoService;

@Controller
@RequestMapping("/inclusaoDisciplina")
public class SolicitacaoMatriculaForaPrazoController {

	protected Logger logger = Logger
			.getLogger(SolicitacaoMatriculaForaPrazoController.class.getName());

	@Autowired
	private SolicitacaoMatriculaForaPrazoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/solicitaInclusaoDisciplinas", method = RequestMethod.POST)
	public String solicitaInclusao(
			@RequestParam("numeroSolicitacoes") int numeroSolicitacoes,
			Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getLogin();
		try {
			service.solicitaInclusao(matricula, numeroSolicitacoes, model);
			return "/inclusaoDisciplina/inclusaoDisciplinaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/homeInclusao", method = RequestMethod.GET)
	public String paginaInicialInclusao(HttpServletRequest request, Model model) {
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matriculaAluno = usr.getLogin();
			service.carregaHomeView(model, matriculaAluno);
			PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
					.getInstance();
			model.addAttribute("periodoLetivo",
					periodoAvaliacao.getPeriodoLetivo());

			return "/inclusaoDisciplina/homeInclusaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/inclusaoDisciplina/inclusaoDisciplinaView";
		}
	}

	@RequestMapping(value = "/incluiSolicitacao", method = RequestMethod.POST)
	public String incluiDisciplina(@RequestParam MultipartFile file,
			@RequestParam String departamento, @RequestParam int opcao,
			@RequestParam String observacao,
			@RequestParam int numeroSolicitacoes, HttpServletRequest request,
			Model model) throws IOException {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getLogin();

		try {
			service.validaSolicitacao(request, matricula, file, departamento,
					opcao, observacao);
			model.addAttribute("sucesso", "Solicitação registrada.");
			service.carregaHomeView(model, matricula);

			return "/inclusaoDisciplina/homeInclusaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			service.solicitaInclusao(matricula, numeroSolicitacoes, model);
			return "/inclusaoDisciplina/inclusaoDisciplinaView";
		}

	}

	@RequestMapping(value = "/listarSolicitacoes", method = RequestMethod.POST)
	public String listarSolicitacoes(@ModelAttribute("login") String matricula,
			@RequestParam int ano, @RequestParam EnumPeriodo periodo,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		try {
			Aluno aluno = service.getAlunoByMatricula(matricula);
			SolicitacaoMatriculaForaPrazo solicitacaoAtual = service
					.getSolicitacaoByAlunoSemestre(aluno.getId(), ano, periodo);

			model.addAttribute("solicitacaoAtual", solicitacaoAtual);
			model.addAttribute("aluno", aluno);

			return "/inclusaoDisciplina/listaSolicitacoesView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			service.carregaHomeView(model, matricula);
			return "/inclusaoDisciplina/homeInclusaoView";
		}
	}

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
