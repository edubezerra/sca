package br.cefetrj.sca.apresentacao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import br.cefetrj.sca.service.AutenticacaoService;
import br.cefetrj.sca.service.AvaliacaoTurmaService;

@Controller
@SessionAttributes("matricula")
@RequestMapping("/avaliacaoTurma")
public class AvaliacaoTurmaController {

	@Autowired
	private AvaliacaoTurmaService service;

	@Autowired
	private AutenticacaoService authService;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/avaliacaoTurmas", method = RequestMethod.GET)
	public String solicitaAvaliacao(HttpSession session, Model model) {
		String matricula = (String) session.getAttribute("matricula");
		model.addAttribute("turmas",
				service.solicitaAvaliacaoMatricula(matricula));
		model.addAttribute("matricula", matricula);
		return "/avaliacaoTurma/solicitaAvaliacaoMatriculaView";
	}

	@RequestMapping(value = "/menuPrincipal", method = RequestMethod.POST)
	public String menuPrincipal(HttpSession session, @RequestParam String matricula,
			@RequestParam String senha, Model model) {

		try {
			authService.autentica(matricula, senha);
			session.setAttribute("matricula", matricula);
			return "/avaliacaoTurma/menuPrincipalView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/solicitaAvaliacaoTurma", method = RequestMethod.POST)
	public String solicitaAvaliacaoTurma(
			@ModelAttribute("matricula") String matricula, // get from session
			@RequestParam String codigoTurma, Model model) {

		try {
			model.addAttribute("questoes",
					service.solicitaAvaliacaoTurma(matricula, codigoTurma));

			return "/avaliacaoTurma/solicitaAvaliacaoTurmaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/avaliacaoTurma/solicitaNovamenteAvaliacaoMatricula";
		}
	}

	@RequestMapping(value = "/avaliaTurma", method = RequestMethod.POST)
	public String avaliaTurma(
			@ModelAttribute("matricula") String matricula, // get from session
			@RequestParam String codigoTurma, @RequestParam String sugestoes,
			HttpServletRequest request, Model model) {

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
			service.avaliaTurma(matricula, codigoTurma, respostas, sugestoes);
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

	
	@RequestMapping(value = "/menuPrincipal")
	public String solicitaNovamenteAvaliacaoMatricula() {
		return "/avaliacaoTurma/menuPrincipalView";
	}

	@RequestMapping(value = "/solicitaNovamenteAvaliacaoMatricula")
	public String solicitaNovamenteAvaliacaoMatricula(
			@ModelAttribute("matricula") String matricula, // get from session
			Model model) {

		try {
			model.addAttribute("turmas",
					service.solicitaAvaliacaoMatricula(matricula));

			return "/avaliacaoTurma/solicitaAvaliacaoMatriculaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "/avaliacaoTurma/solicitaAvaliacaoView";
		}
	}
}
