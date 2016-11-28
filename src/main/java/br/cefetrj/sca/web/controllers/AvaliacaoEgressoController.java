package br.cefetrj.sca.web.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.AvaliacaoEgressoService;
import br.cefetrj.sca.service.util.ValidacaoHelper;
import br.cefetrj.sca.service.util.ValidacaoResponse;

@Controller
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
	public String solicitaAvaliacaoGraduacao(Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		Aluno aluno = service.findAlunoByMatricula(matricula);
		if (aluno != null) {
			try {
				SolicitaAvaliacaoEgressoResponse resp = service
						.retornaQuestoes();
				model.addAttribute("questoes", resp);
				model.addAttribute("matricula", matricula);
				model.addAttribute("nomeCurso", aluno.getVersaoCurso()
						.getCurso());
				return "/avaliacaoEgresso/questionarioGraduacao";
			} catch (Exception exc) {
				model.addAttribute("error", exc.getMessage());
				return "/homeView";
			}
		} else {
			model.addAttribute("error", "Aluno não encontrado!");
			return "/homeView";
		}
	}

	@RequestMapping(value = "/questionarioMedio", method = RequestMethod.GET)
	public String solicitaAvaliacaoMedio(Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			SolicitaAvaliacaoEgressoResponse quesitos = service
					.retornaQuestoes();
			quesitos.remove(quesitos.size() - 1);
			model.addAttribute("questoes", quesitos);
			model.addAttribute("matricula", matricula);
			return "/avaliacaoEgresso/questionarioMedio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/escolherAvaliacao", method = RequestMethod.GET)
	public String escolherAvaliacao(Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("matricula", matricula);
			return "/avaliacaoEgresso/escolherAvaliacao";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/confirmacaoEnvio", method = RequestMethod.POST)
	public String confirmacaoEnvio(Model model) {
		try {
			return "/avaliacaoEgresso/confirmacaoEnvio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/menuPrincipal")
	public String solicitaNovamenteAvaliacaoMatricula(Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		if (matricula != null) {
			PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
					.getInstance();
			model.addAttribute("periodoLetivo",
					periodoAvaliacao.getPeriodoLetivo());
			return "/avaliacaoTurma/menuPrincipalView";
		} else {
			return "/homeView";
		}
	}

	@RequestMapping(value = "/avaliaEgresso", method = RequestMethod.POST)
	public String avaliaEgresso(HttpServletRequest request, Model model) {
		int numeroDeQuestoes = 16;
		Map<String, String[]> parameters = request.getParameterMap();
		List<Long> respostas = new ArrayList<>();
		String questao10Outro = "";
		String questao15Area = "";
		String especialidade = "";

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();

		if (matricula == null || matricula.isEmpty()) {
			model.addAttribute("error",
					"Erro: O usuário não está ativo na sessão.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}

		if (parameters.containsKey("resp-especialidade")) {
			especialidade = parameters.get("resp-especialidade")[0];
		}

		try {
			for (int i = 1; i <= numeroDeQuestoes; i++) {
				String nomeQuestao = "question-" + i;
				if (!parameters.containsKey(nomeQuestao))
					continue;
				String resposta = parameters.get(nomeQuestao)[0];
				if (resposta.equals("37")) {
					questao10Outro = parameters.get("resp-10")[0];
				}

				if (resposta.equals("55")) {
					questao15Area = parameters.get("resp-area")[0];
				}
				respostas.add(Long.parseLong(resposta));
			}

			if (respostas.size() == 0) {
				model.addAttribute("error",
						"Erro: o formulário deve ser preenchido.");
				return "forward:/avaliacaoEgresso/confirmacaoEnvio";
			}

			if (respostas.size() > 5
					&& (especialidade == null || especialidade == "")) {
				model.addAttribute("error",
						"Erro: A pergunta 7 sobre sua especialidade deve ser respondida.");
				return "forward:/avaliacaoEgresso/confirmacaoEnvio";
			}

			ValidacaoHelper helper = new ValidacaoHelper();
			ValidacaoResponse validacao = helper.validaRespostasEgresso(
					respostas, "Graduacao");
			if (!validacao.isValid()) {
				model.addAttribute(
						"error",
						"Erro: Houve um ou mais erros de validação das respostas: "
								+ validacao.message()
								+ " Leia atentamente as questões e responda corretamente.");
				return "forward:/avaliacaoEgresso/confirmacaoEnvio";
			}
		} catch (Exception exc) {
			model.addAttribute("error",
					"Erro: Respostas com conteúdo inválido.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}

		try {
			service.avaliaEgresso(matricula, respostas, especialidade,
					questao10Outro, questao15Area);
			model.addAttribute(
					"info",
					"O formulário foi enviado com sucesso! "
							+ "Obrigado por colaborar com o acompanhamento de egressos do CEFET/RJ.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}
	}

	@RequestMapping(value = "/avaliaEgressoMedio", method = RequestMethod.POST)
	public String avaliaEgressoMedio(HttpServletRequest request, Model model) {
		int numeroDeQuestoes = 16;
		Map<String, String[]> parameters = request.getParameterMap();
		List<Long> respostas = new ArrayList<>();
		String questao10Outro = "";
		String questao15Area = "";
		String especialidade = "";

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();

		if (parameters.containsKey("resp-especialidade")) {
			especialidade = parameters.get("resp-especialidade")[0];
		}

		try {
			for (int i = 1; i <= numeroDeQuestoes; i++) {
				String nomeQuestao = "question-" + i;
				if (!parameters.containsKey(nomeQuestao))
					continue;
				String resposta = parameters.get(nomeQuestao)[0];
				if (resposta.equals("37")) {
					questao10Outro = parameters.get("resp-10")[0];
				}

				if (resposta.equals("55")) {
					questao15Area = parameters.get("resp-area")[0];
				}
				respostas.add(Long.parseLong(resposta));
			}

			if (respostas.size() == 0) {
				model.addAttribute("error",
						"Erro: o formulário deve ser preenchido.");
				return "forward:/avaliacaoEgresso/confirmacaoEnvio";
			}

			if (matricula == null || matricula.isEmpty()) {
				model.addAttribute("error",
						"Erro: usuário não está ativo na sessão.");
				return "forward:/avaliacaoEgresso/confirmacaoEnvio";
			}

			if (respostas.size() > 5
					&& (especialidade == null || especialidade == "")) {
				model.addAttribute("error",
						"Erro: A pergunta 7 sobre sua especialidade deve ser respondida.");
				return "forward:/avaliacaoEgresso/confirmacaoEnvio";
			}

			ValidacaoHelper helper = new ValidacaoHelper();
			ValidacaoResponse validacao = helper.validaRespostasEgresso(
					respostas, "Medio");
			if (!validacao.isValid()) {
				model.addAttribute(
						"error",
						"Erro: Houve um ou mais erros de validação das respostas: "
								+ validacao.message()
								+ " Leia atentamente as questões e responda corretamente.");
				return "forward:/avaliacaoEgresso/confirmacaoEnvio";
			}
		} catch (Exception exc) {
			model.addAttribute("error",
					"Erro: Respostas com conteúdo inválido.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}

		try {
			service.avaliaEgresso(matricula, respostas, especialidade,
					questao10Outro, questao15Area);
			model.addAttribute(
					"info",
					"Seu formulário foi enviado com sucesso! Obrigado por responder o questionário.");
			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/avaliacaoEgresso/confirmacaoEnvio";
		}
	}
}
