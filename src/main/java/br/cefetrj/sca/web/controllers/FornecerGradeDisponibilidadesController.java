package br.cefetrj.sca.web.controllers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidade;
import br.cefetrj.sca.service.FornecerGradeDisponibilidadeService;

@Controller
@SessionAttributes("cpf")
@RequestMapping("/gradedisponibilidades")
public class FornecerGradeDisponibilidadesController {

	protected Logger logger = Logger
			.getLogger(FornecerGradeDisponibilidadesController.class.getName());

	@Autowired
	private FornecerGradeDisponibilidadeService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/apresentarFormulario", method = RequestMethod.GET)
	public String apresentarFormulario(HttpServletRequest request,
			HttpServletResponse response, Model model, HttpSession session) {
		return "/gradeDisponibilidades/gradeDisponibilidadesView";
	}

	/**
	 * @param matriculaProfessor
	 * @return
	 */
	@RequestMapping(value = "/validarProfessor", method = RequestMethod.POST)
	public String validarProfessor(
			@RequestParam("matriculaProfessor") String matriculaProfessor,
			Model model) {

		System.err.println(matriculaProfessor);

		FichaDisponibilidade ficha = service
				.validarProfessor(matriculaProfessor);

		model.addAttribute("habilitacoes", ficha.getHabilitacoes());
		return "/gradeDisponibilidades/disponibilidadesView";
	}

	@RequestMapping(value = "/solicitaAdicaoDisciplina", method = RequestMethod.POST)
	public String avaliaTurma(@RequestParam String codigoDisciplina, Model model) {
		try {
			service.adicionarDisciplina(codigoDisciplina);
			model.addAttribute("info", "Disciplina adicionada com sucesso!");
		} catch (IllegalArgumentException e) {
			model.addAttribute("error", e.getMessage());
		}
		return "/gradeDisponibilidades/disponibilidadesView";
	}
}