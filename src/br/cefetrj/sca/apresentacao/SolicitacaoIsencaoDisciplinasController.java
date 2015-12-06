package br.cefetrj.sca.apresentacao;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.isencoes.SolicitacaoIsencaoDisciplinas;
import br.cefetrj.sca.service.SolicitacaoIsencaoDisciplinasService;

@Controller
@SessionAttributes("login")
@RequestMapping("/isencaoDisciplinas")
public class SolicitacaoIsencaoDisciplinasController {

	protected Logger logger = Logger
			.getLogger(SolicitacaoIsencaoDisciplinasController.class.getName());

	@Autowired
	private SolicitacaoIsencaoDisciplinasService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/homeInclusao", method = RequestMethod.GET)
	public String paginaInicialInclusao(@ModelAttribute("login") String cpf,
			HttpServletRequest request, Model model, HttpSession session) {
		try {
			SolicitacaoIsencaoDisciplinas solicitacao = service
					.getSolicitacaoAtual(cpf);
			Aluno aluno = service.getAlunoByCpf(cpf);
			model.addAttribute("aluno", aluno);
			session.setAttribute("aluno", aluno);
			model.addAttribute("solicitacao", solicitacao);
			return "/isencaoDisciplinas/homeInclusaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/iniciaSolicitaIsencaoDisciplinas", method = RequestMethod.POST)
	public String solicitaInclusao(HttpSession session, Model model) {
		String login = (String) session.getAttribute("login");
		try {
			service.solicitaInclusao(login, model);
			return "/isencaoDisciplinas/inclusaoDisciplinaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/incluiSolicitacao", method = RequestMethod.POST)
	public String incluiDisciplina(@ModelAttribute("login") String cpf,
			@RequestParam MultipartFile file,
			@RequestParam String departamento, @RequestParam String observacao,
			HttpServletRequest request, Model model) throws IOException {

		try {
			service.registrarSolicitacao(request, cpf, file, departamento,
					observacao);
			model.addAttribute("sucesso", "Solicitação registrada.");
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			service.solicitaInclusao(cpf, model);
			return "/isencaoDisciplinas/inclusaoDisciplinaView";
		}
		return "/isencaoDisciplinas/homeInclusaoView";
	}

	@RequestMapping(value = "/listarSolicitacoes", method = RequestMethod.POST)
	public String listarSolicitacoes(@ModelAttribute("login") String cpf,
			HttpServletRequest request, HttpServletResponse response,
			Model model) {

		try {
			Aluno aluno = service.getAlunoByCpf(cpf);
			SolicitacaoIsencaoDisciplinas solicitacaoAtual = service
					.getSolicitacaoByAluno(aluno.getId());

			model.addAttribute("solicitacaoAtual", solicitacaoAtual);
			model.addAttribute("aluno", aluno);

			return "/isencaoDisciplinas/listaSolicitacoesView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/isencaoDisciplinas/homeInclusaoView";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute("login") String cpf,
			@RequestParam Long solicitacaoId, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			Comprovante comprovante = service.getComprovante(solicitacaoId);
			GerenteArquivos.downloadFile(cpf, solicitacaoId, request, response,
					comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
