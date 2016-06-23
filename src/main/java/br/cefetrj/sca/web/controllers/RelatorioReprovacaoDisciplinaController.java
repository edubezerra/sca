package br.cefetrj.sca.web.controllers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.cefetrj.sca.service.RelatorioReprovacaoDisciplinaService;

@Controller
@RequestMapping("/relatorioReprovacaoDisciplina")
public class RelatorioReprovacaoDisciplinaController {

	@Autowired
	private RelatorioReprovacaoDisciplinaService service;

	protected Logger logger = Logger.getLogger(RelatorioReprovacaoDisciplinaController.class.getName());

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/homeReprovacaoDisciplina", method = RequestMethod.GET)
	public String homeReprovacaoDisciplina(HttpServletRequest request, Model model) {
		try {

			return "/relatorioReprovacaoDisciplina/homeReprovacaoDisciplinaView";

		} catch (Exception exc) {

			model.addAttribute("error", exc.getMessage());
			return "/homeView";

		}
	}

	@RequestMapping(value = "/relatorioReprovacaoDisciplina", method = RequestMethod.POST)
	public String relatorioReprovacaoDisciplina(HttpServletRequest request, Model model,
			@RequestParam String disciplina) {
		try {

			String response = service.createDataResponse(disciplina);

			model.addAttribute("response", response);
			model.addAttribute("disciplina", disciplina);
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
		}

		return "/relatorioReprovacaoDisciplina/homeReprovacaoDisciplinaView";
	}
}
