package br.cefetrj.sca.apresentacao;

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
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.dominio.inclusaodisciplina.SolicitacaoMatriculaForaPrazo;
import br.cefetrj.sca.service.SolicitacaoMatriculaForaPrazoService;

@Controller
@SessionAttributes("login")
@RequestMapping("/inclusaoDisciplina")
public class SolicitacaoMatriculaForaPrazoController {

	protected Logger logger = Logger.getLogger(SolicitacaoMatriculaForaPrazoController.class.getName());

	@Autowired
	private SolicitacaoMatriculaForaPrazoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/solicitaInclusaoDisciplinas", method = RequestMethod.POST)
	public String solicitaInclusao(HttpSession session, @RequestParam("numeroSolicitacoes") int numeroSolicitacoes,
			Model model) {
		String login = (String) session.getAttribute("login");
		try {
			service.solicitaInclusao(login, numeroSolicitacoes, model);
			return "/inclusaoDisciplina/inclusaoDisciplinaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/homeInclusao", method = RequestMethod.GET)
	public String paginaInicialInclusao(@ModelAttribute("login") String cpf, HttpServletRequest request, Model model) {
		try {
			SolicitacaoMatriculaForaPrazo solicitacaoAtual = service.getSolicitacaoAtual(cpf);
			if (solicitacaoAtual != null) {
				List<SolicitacaoMatriculaForaPrazo> solicitacoes = service
						.getSolicitacoesAluno(solicitacaoAtual.getAluno().getId());
				List<SemestreLetivo> listaSemestresLetivos = SolicitacaoMatriculaForaPrazo
						.semestresCorrespondentes(solicitacoes);
				model.addAttribute("listaSemestresLetivos", listaSemestresLetivos);
			}

			model.addAttribute("aluno", service.getAlunoByCpf(cpf));

			if (solicitacaoAtual != null) {
				model.addAttribute("numeroSolicitacoes", solicitacaoAtual.getItensSolicitacao().size());
			} else {
				model.addAttribute("numeroSolicitacoes", 0);
			}

			PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas.getInstance();
			model.addAttribute("periodoLetivo", periodoAvaliacao.getSemestreLetivo());

			return "/inclusaoDisciplina/homeInclusaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/incluiSolicitacao", method = RequestMethod.POST)
	public String incluiDisciplina(@ModelAttribute("login") String cpf, @RequestParam MultipartFile file,
			@RequestParam String departamento, @RequestParam int opcao, @RequestParam String observacao,
			@RequestParam int numeroSolicitacoes, HttpServletRequest request, HttpSession session, Model model)
					throws IOException {

		try {
			service.validaSolicitacao(request, cpf, file, departamento, opcao, observacao);
			model.addAttribute("sucesso", "Solicitação registrada.");
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			service.solicitaInclusao(cpf, numeroSolicitacoes, model);
			return "/inclusaoDisciplina/inclusaoDisciplinaView";
		}

//		service.getSolicitacaoAtual(cpf, model);
		return "/inclusaoDisciplina/homeInclusaoView";
	}

	@RequestMapping(value = "/listarSolicitacoes", method = RequestMethod.POST)
	public String listarSolicitacoes(@ModelAttribute("login") String cpf, @RequestParam int ano,
			@RequestParam EnumPeriodo periodo, HttpServletRequest request, HttpServletResponse response, Model model) {

		try {
			Aluno aluno = service.getAlunoByCpf(cpf);
			SolicitacaoMatriculaForaPrazo solicitacaoAtual = service.getSolicitacaoByAlunoSemestre(aluno.getId(), ano,
					periodo);

			model.addAttribute("solicitacaoAtual", solicitacaoAtual);
			model.addAttribute("aluno", aluno);

			return "/inclusaoDisciplina/listaSolicitacoesView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
//			service.getSolicitacaoAtual(cpf, model);
			return "/inclusaoDisciplina/homeInclusaoView";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@ModelAttribute("login") String cpf, @RequestParam Long solicitacaoId,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Comprovante comprovante = service.getComprovante(solicitacaoId);
			GerenteArquivos.downloadFile(cpf, solicitacaoId, request, response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
