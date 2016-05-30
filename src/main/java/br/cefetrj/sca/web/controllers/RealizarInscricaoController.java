package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.RealizarInscricaoService;

@Controller
@RequestMapping("/realizarInscricao")
public class RealizarInscricaoController {

	protected Logger logger = Logger.getLogger(RealizarInscricaoController.class.getName());

	@Autowired
	private RealizarInscricaoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/realizarInscricao", method = RequestMethod.GET)
	public String realizarInscricao(String matriculaAluno2, HttpServletRequest request, Model model) {
		try {

			Usuario usr = UsuarioController.getCurrentUser();
			String matriculaAluno = usr.getLogin();
			Aluno aluno = service.findAlunoByMatricula(matriculaAluno);

			List<Disciplina> disciplinasPossiveis = aluno.getDisciplinasPossiveis();
			List<Turma> turmasPossiveis = service.getTurmasPossiveis(aluno, disciplinasPossiveis);

			model.addAttribute("aluno", aluno);
			model.addAttribute("semestreLetivo", PeriodoLetivo.PERIODO_CORRENTE);
			model.addAttribute("turmasPossiveis", turmasPossiveis);
			return "/realizarInscricao/realizarInscricaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/registrarTurmas", method = RequestMethod.GET)
	public String registrarTurmas(HttpServletRequest request, HttpSession session,
			Model model) throws IOException {

		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getLogin();
		Aluno aluno = service.findAlunoByMatricula(matriculaAluno);

		try {
			if (request.getParameterValues("checkbox") != null) {
				List<String> turmasIds = Arrays.asList(request.getParameterValues("checkbox"));
				List<Turma> turmas = new ArrayList<Turma>();
				HashMap<Turma, String> turmasInscritas = new HashMap<Turma, String>();
				for (String id : turmasIds) {
					turmas.add(service.findTurmaById(Long.parseLong(id)));
				}
				for (Turma turma : turmas) {
					String resultado = service.registrarInscricao(turma, aluno);
					turmasInscritas.put(turma, resultado);
				}
				model.addAttribute("inscritas", turmasInscritas);
				model.addAttribute("semestreLetivo", PeriodoLetivo.PERIODO_CORRENTE);
				model.addAttribute("aluno", aluno);
			} else {
				model.addAttribute("msg", "Selecione uma ou mais turmas!");
				return realizarInscricao(matriculaAluno, request, model);
			}
			return "/realizarInscricao/inscricaoRegistradaSituacao";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return realizarInscricao(matriculaAluno, request, model);
		}
	}

	@RequestMapping(value = "/menuPrincipal")
	public String voltar(HttpSession session, Model model) {

		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAluno = usr.getLogin();

		if (matriculaAluno != null) {

			//TODO: parametrizar o período de inscrição.
			model.addAttribute("periodoLetivo", PeriodoLetivo.PERIODO_CORRENTE);
			// model.addAttribute("periodoLetivo",
			// PeriodoLetivo.PERIODO_CORRENTE.sucessor());

			return "/menuPrincipalView";
		} else {
			return "/homeView";
		}
	}
}
