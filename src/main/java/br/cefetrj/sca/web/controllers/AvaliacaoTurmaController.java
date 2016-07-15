package br.cefetrj.sca.web.controllers;

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

import br.cefetrj.sca.service.AvaliacaoTurmaService;
import br.cefetrj.sca.service.util.SolicitaAvaliacaoResponse;
import br.cefetrj.sca.service.util.SolicitaAvaliacaoTurmaResponse;

@Controller
@SessionAttributes("matricula")
@RequestMapping("/avaliacaoTurma")
public class AvaliacaoTurmaController {

	protected Logger logger = Logger.getLogger(AvaliacaoTurmaController.class.getName());

	@Autowired
	private AvaliacaoTurmaService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/avaliacaoTurmas", method = RequestMethod.GET)
	public String solicitaAvaliacao(HttpSession session, Model model) {
		String matricula = UsuarioController.getCurrentUser().getMatricula();
		session.setAttribute("login", matricula);
		try {
			SolicitaAvaliacaoResponse turmasCursadas = service.obterTurmasCursadas(matricula);
			model.addAttribute("turmasCursadas", turmasCursadas);
			model.addAttribute("matricula", matricula);
			return "/avaliacaoTurma/apresentaListagemTurmasView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/menuPrincipalView";
		}
	}

	@RequestMapping(value = "/menuPrincipal")
	public String solicitaNovamenteAvaliacaoTurma() {
		return "/menuPrincipalView";
	}

	@RequestMapping(value = "/solicitaAvaliacaoTurma", method = RequestMethod.POST)
	public String solicitaAvaliacaoTurma(@ModelAttribute("matricula") String matricula, @RequestParam String idTurma,
			Model model) {

		try {
			Long id = Long.parseLong(idTurma);
			SolicitaAvaliacaoTurmaResponse resp = service.solicitaAvaliacaoTurma(matricula, id);
			model.addAttribute("questoes", resp);
			model.addAttribute("nomeDisciplina", resp.getNomeDisciplina());
			model.addAttribute("codigoTurma", resp.getCodigoTurma());
			model.addAttribute("idTurma", idTurma);

			return "/avaliacaoTurma/solicitaAvaliacaoTurmaView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/avaliacaoTurma/solicitaNovamenteAvaliacaoTurma";
		}
	}

	@RequestMapping(value = "/avaliaTurma", method = RequestMethod.POST)
	public String avaliaTurma(@ModelAttribute("matricula") String matricula, @RequestParam String idTurma,
			@RequestParam String aspectosPositivos, @RequestParam String aspectosNegativos, HttpServletRequest request,
			Model model) {

		Map<String, String[]> parameters = request.getParameterMap();
		List<Integer> respostas = new ArrayList<Integer>();

		try {
			int i = 0;

			// parameters must contain only sorted quesitoX parameters
			while (parameters.containsKey("quesito" + i)) {
				respostas.add(Integer.parseInt(parameters.get("quesito" + i)[0]));
				++i;
			}
		} catch (Exception exc) {
			model.addAttribute("error", "Erro: Respostas com conteúdo inválido.");
			model.addAttribute("idTurma", idTurma);

			return "forward:/avaliacaoTurma/solicitaAvaliacaoTurma";
		}

		try {
			Long id = Long.parseLong(idTurma);
			System.out.println("AvaliacaoTurmaController.avaliaTurma()");
			service.avaliaTurma(matricula, id, respostas, aspectosPositivos, aspectosNegativos);
			model.addAttribute("info", "Avaliação registrada.");
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			model.addAttribute("idTurma", idTurma);

			int i = 0;

			// parameters must contain only sorted quesitoX parameters
			while (i < parameters.size()) {
				if (parameters.get("quesito" + i) != null) {
					model.addAttribute("oldQuesito" + i, parameters.get("quesito" + i)[0]);
				}
				++i;
			}

			model.addAttribute("aspectosPositivos", aspectosPositivos);
			model.addAttribute("aspectosNegativos", aspectosNegativos);

			return "forward:/avaliacaoTurma/solicitaAvaliacaoTurma";
		}

		return "forward:/avaliacaoTurma/solicitaNovamenteAvaliacaoTurma";
	}

	@RequestMapping(value = "/solicitaNovamenteAvaliacaoTurma")
	public String solicitaNovamenteAvaliacaoTurma(@ModelAttribute("matricula") String matricula, Model model) {

		try {
			model.addAttribute("turmasCursadas", service.obterTurmasCursadas(matricula));

			return "/avaliacaoTurma/apresentaListagemTurmasView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "/avaliacaoTurma/solicitaAvaliacaoView";
		}
	}
}
