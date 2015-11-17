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
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.service.AvaliacaoEgressoService;

@Controller
@SessionAttributes("login")
@RequestMapping("/avaliacaoEgresso")
public class AvaliacaoEgressoController {

	protected Logger logger = Logger.getLogger(AvaliacaoEgressoController.class
			.getName());

	@Autowired
	private AvaliacaoEgressoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}


	@RequestMapping(value = "/questionarioGraduacao", method = RequestMethod.GET)
	public String solicitaAvaliacaoGraduacao(HttpSession session, Model model) {
		String cpf = (String) session.getAttribute("login");
		try {
			SolicitaAvaliacaoEgressoResponse resp = service.retornaQuestoes();		
			model.addAttribute("questoes", resp);
			model.addAttribute("login", cpf);
			return "/avaliacaoEgresso/questionarioGraduacao";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	
	@RequestMapping(value = "/questionarioMedio", method = RequestMethod.GET)
	public String solicitaAvaliacaoMedio(HttpSession session, Model model) {
		String cpf = (String) session.getAttribute("login");

		try {
			SolicitaAvaliacaoEgressoResponse quesitos = service.retornaQuestoes();	
			quesitos.remove(quesitos.size() - 1);
			model.addAttribute("questoes", quesitos);
			model.addAttribute("login", cpf);
			return "/avaliacaoEgresso/questionarioMedio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	
	@RequestMapping(value = "/escolherAvaliacao", method = RequestMethod.GET)
	public String escolherAvaliacao(HttpSession session, Model model) {
		String cpf = (String) session.getAttribute("login");

		try {
			model.addAttribute("login", cpf);
			return "/avaliacaoEgresso/escolherAvaliacao";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
	@RequestMapping(value = "/confirmacaoEnvio", method = RequestMethod.POST)
	public String confirmacaoEnvio(HttpSession session, Model model) {
		try {
			return "/avaliacaoEgresso/confirmacaoEnvio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/menuPrincipal")
	public String solicitaNovamenteAvaliacaoMatricula(HttpSession session,
			Model model) {
		String cpf = (String) session.getAttribute("login");
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

	@RequestMapping(value = "/avaliaEgresso", method = RequestMethod.POST)
	public String avaliaTurma(
			@ModelAttribute("login") String cpf, // get from session
			HttpServletRequest request,
			Model model) {
		
		int numeroDeQuestoes = 16;
		Map<String, String[]> parameters = request.getParameterMap();
		List<Integer> respostas = new ArrayList<Integer>();
		String questao10Outro = "";
		String questao15Area = "";
		String especialidade = "";
		
		if(parameters.containsKey("resp-especialidade")) {
			especialidade = parameters.get("resp-especialidade")[0];
		}
		
		try {
				for(int i=1; i <= numeroDeQuestoes; i++)
				{
					String nomeQuestao = "question-" + i;
					if(!parameters.containsKey(nomeQuestao)) continue;
					String resposta = parameters.get(nomeQuestao)[0];
					if(resposta.equals("37")){
						questao10Outro = parameters.get("resp-10")[0];
					}
					
					if(resposta.equals("55")){
						questao15Area = parameters.get("resp-area")[0];
					}
					respostas.add(Integer.parseInt(resposta));
				}
				
		} catch (Exception exc) {
			model.addAttribute("error", "Erro: Respostas com conteúdo inválido.");
			return "forward:/avaliacaoTurma/solicitaAvaliacaoTurma";
		}

		try {
			service.avaliaEgresso(cpf, respostas, especialidade, questao10Outro, questao15Area);
			model.addAttribute("info", "Seu formulário foi enviado com sucesso! Obrigado por responder o questionário.");
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/avaliacaoTurma/solicitaAvaliacaoTurma";
		}

		return "forward:/avaliacaoEgresso/confirmacaoEnvio";
	}

}


