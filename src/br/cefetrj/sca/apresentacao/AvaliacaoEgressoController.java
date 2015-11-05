package br.cefetrj.sca.apresentacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.service.AutenticacaoService;
import br.cefetrj.sca.service.AvaliacaoEgressoService;
import br.cefetrj.sca.service.util.SolicitaAvaliacaoTurmaResponse;

@Controller
@SessionAttributes("cpf")
@RequestMapping("/avaliacaoEgresso")
public class AvaliacaoEgressoController {

	protected Logger logger = Logger.getLogger(AvaliacaoEgressoController.class
			.getName());

	@Autowired
	private AvaliacaoEgressoService service;

	@Autowired
	private AutenticacaoService authService;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}


	@RequestMapping(value = "/questionarioGraduacao", method = RequestMethod.GET)
	public String solicitaAvaliacaoGraduacao(HttpSession session, Model model) {
		String cpf = (String) session.getAttribute("cpf");

		try {
			model.addAttribute("cpf", cpf);
			return "/avaliacaoEgresso/questionarioGraduacao";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	@RequestMapping(value = "/questionarioMedio", method = RequestMethod.GET)
	public String solicitaAvaliacaoMedio(HttpSession session, Model model) {
		String cpf = (String) session.getAttribute("cpf");

		try {
			model.addAttribute("cpf", cpf);
			return "/avaliacaoEgresso/questionarioMedio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	@RequestMapping(value = "/escolherAvaliacao", method = RequestMethod.GET)
	public String escolherAvaliacao(HttpSession session, Model model) {
		String cpf = (String) session.getAttribute("cpf");

		try {
			model.addAttribute("cpf", cpf);
			return "/avaliacaoEgresso/escolherAvaliacao";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/menuPrincipal")
	public String solicitaNovamenteAvaliacaoMatricula(HttpSession session,
			Model model) {
		String cpf = (String) session.getAttribute("cpf");
		if (cpf != null) {
			PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
					.getInstance();
			model.addAttribute("periodoLetivo",
					periodoAvaliacao.getSemestreLetivo());
			return "/avaliacaoTurma/menuPrincipalView";
		} else {
			return "/homeView";
		}
	}

	@RequestMapping(value = "/menuPrincipal", method = RequestMethod.POST)
	public String menuPrincipal(HttpSession session, @RequestParam String cpf,
			@RequestParam String senha, Model model) {

		try {
			authService.autentica(cpf, senha);
			session.setAttribute("cpf", cpf);
			PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
					.getInstance();
			model.addAttribute("periodoLetivo",
					periodoAvaliacao.getSemestreLetivo());
			return "/avaliacaoTurma/menuPrincipalView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/solicitaAvaliacaoEgresso", method = RequestMethod.GET)
	public String solicitaAvaliacaoTurma(@ModelAttribute("cpf") String cpf, // get
																			// from
																			// session
			@RequestParam String codigoTurma, Model model) {

		try {
			SolicitaAvaliacaoTurmaResponse resp = service
					.solicitaAvaliacaoEgresso(cpf, codigoTurma);
			model.addAttribute("questoes", resp);
			model.addAttribute("nomeDisciplina", resp.getNomeDisciplina());
			model.addAttribute("codigoTurma", resp.getCodigoTurma());

			return "/avaliacaoTurma/solicitaAvaliacaoTurmaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/avaliacaoTurma/solicitaNovamenteAvaliacaoMatricula";
		}
	}

	@RequestMapping(value = "/avaliaTurma", method = RequestMethod.POST)
	public String avaliaTurma(
			@ModelAttribute("cpf") String cpf, // get from session
			@RequestParam String codigoTurma,
			@RequestParam String aspectosPositivos,
			@RequestParam String aspectosNegativos, HttpServletRequest request,
			Model model) {

		Map<String, String[]> parameters = request.getParameterMap();
		List<Integer> respostas = new ArrayList<Integer>();

		try {
			int i = 0;

			// parameters must contain only sorted quesitoX parameters
			while (parameters.containsKey("quesito" + i)) {
				respostas
						.add(Integer.parseInt(parameters.get("quesito" + i)[0]));
				++i;
			}
		} catch (Exception exc) {
			model.addAttribute("error",
					"Erro: Respostas com conteúdo inválido.");
			model.addAttribute("codigoTurma", codigoTurma);

			return "forward:/avaliacaoTurma/solicitaAvaliacaoTurma";
		}

		try {
			service.avaliaEgresso(cpf, codigoTurma, respostas, aspectosPositivos,
					aspectosNegativos);
			model.addAttribute("info", "Avaliação registrada.");
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			model.addAttribute("codigoTurma", codigoTurma);

			int i = 0;

			// parameters must contain only sorted quesitoX parameters
			while (parameters.containsKey("quesito" + i)) {
				model.addAttribute("oldQuesito" + i,
						parameters.get("quesito" + i)[0]);
				++i;
			}

			return "forward:/avaliacaoTurma/solicitaAvaliacaoTurma";
		}

		return "forward:/avaliacaoTurma/solicitaNovamenteAvaliacaoMatricula";
	}

}
