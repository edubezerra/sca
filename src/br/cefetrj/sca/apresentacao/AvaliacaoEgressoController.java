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
import br.cefetrj.sca.service.AutenticacaoService;
import br.cefetrj.sca.service.AvaliacaoEgressoService;
import br.cefetrj.sca.service.util.ValidacaoHelper;
import br.cefetrj.sca.service.util.ValidacaoResponse;

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
			SolicitaAvaliacaoEgressoResponse resp = service.retornaQuestoes();		
			model.addAttribute("questoes", resp);
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
			SolicitaAvaliacaoEgressoResponse quesitos = service.retornaQuestoes();	
			quesitos.remove(quesitos.size() - 1);
			model.addAttribute("questoes", quesitos);
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

	@RequestMapping(value = "/avaliaEgresso", method = RequestMethod.POST)
	public String avaliaEgresso(
			@ModelAttribute("cpf") String cpf, // get from session
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
				
				if(respostas.size() == 0){
					model.addAttribute("error", "Erro: o formulário deve ser preenchido.");
					return "forward:/avaliacaoEgresso/confirmacaoEnvio";
				}
				
				if(cpf == null || cpf == ""){
					model.addAttribute("error", "Erro: O login (CPF) não está ativo na sessão.");
					return "forward:/avaliacaoEgresso/confirmacaoEnvio";
				}
				
				if(respostas.size() > 5 && (especialidade == null || especialidade == "")){
					model.addAttribute("error", "Erro: A pergunta 7 sobre sua especialidade deve ser respondida.");
					return "forward:/avaliacaoEgresso/confirmacaoEnvio";
				}
				
				ValidacaoHelper helper = new ValidacaoHelper();
				ValidacaoResponse validacao = helper.validaRespostasEgresso(respostas, "Graduacao");
				if(!validacao.isValid()){
					model.addAttribute("error", "Erro: Houveram um ou mais erros de validação das respostas: " + validacao.message() + " Leia atentamente as questões e responda corretamente.");
					return "forward:/avaliacaoEgresso/confirmacaoEnvio";
				}
		} catch (Exception exc) {
			model.addAttribute("error", "Erro: Respostas com conteúdo inválido.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}

		try {
			service.avaliaEgresso(cpf, respostas, especialidade, questao10Outro, questao15Area);
			model.addAttribute("info", "Seu formulário foi enviado com sucesso! Obrigado por responder o questionário.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}
	}
	
	
	@RequestMapping(value = "/avaliaEgressoMedio", method = RequestMethod.POST)
	public String avaliaEgressoMedio(
			@ModelAttribute("cpf") String cpf, // get from session
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
				
				if(respostas.size() == 0){
					model.addAttribute("error", "Erro: o formulário deve ser preenchido.");
					return "forward:/avaliacaoEgresso/confirmacaoEnvio";
				}
				
				if(cpf == null || cpf == ""){
					model.addAttribute("error", "Erro: O login (CPF) não está ativo na sessão.");
					return "forward:/avaliacaoEgresso/confirmacaoEnvio";
				}
				
				if(respostas.size() > 5 && (especialidade == null || especialidade == "")){
					model.addAttribute("error", "Erro: A pergunta 7 sobre sua especialidade deve ser respondida.");
					return "forward:/avaliacaoEgresso/confirmacaoEnvio";
				}
				
				ValidacaoHelper helper = new ValidacaoHelper();
				ValidacaoResponse validacao = helper.validaRespostasEgresso(respostas, "Medio");
				if(!validacao.isValid()){
					model.addAttribute("error", "Erro: Houveram um ou mais erros de validação das respostas: " + validacao.message() + " Leia atentamente as questões e responda corretamente.");
					return "forward:/avaliacaoEgresso/confirmacaoEnvio";
				}
		} catch (Exception exc) {
			model.addAttribute("error", "Erro: Respostas com conteúdo inválido.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}

		try {
			service.avaliaEgresso(cpf, respostas, especialidade, questao10Outro, questao15Area);
			model.addAttribute("info", "Seu formulário foi enviado com sucesso! Obrigado por responder o questionário.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}
	}

	
}


