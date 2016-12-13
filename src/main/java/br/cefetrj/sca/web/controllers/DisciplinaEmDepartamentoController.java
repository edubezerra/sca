package br.cefetrj.sca.web.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.DisciplinaEmDepartamentoService;

@Controller
@RequestMapping("/alocacaoDisciplinaDepartamento")
public class DisciplinaEmDepartamentoController {

	@Autowired
	private DisciplinaEmDepartamentoService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/login";
	}

	@RequestMapping(value = "/menuPrincipal")
	public String menuPrincipal(HttpSession session, Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		if (matricula != null) {
			return "/menuPrincipalView";
		} else {
			return "/login";
		}
	}

	@RequestMapping(value = { "/", "/homeAlocacaoDisciplinas" }, method = RequestMethod.GET)
	public String paginaInicialAlocacaoCoordenadorAtividades(
			HttpServletRequest request, HttpSession session, Model model) {
		try {
			model.addAttribute("departamentos", service.listarDepartamentos());
			model.addAttribute("disciplinas", service.findDisciplinas());
			model.addAttribute("lotacoes", service.findAlocacoesDisciplinas());

			return "/alocacaoDisciplinaDepartamento/alocaDisciplinaDepartamentoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/alocacaoDisciplinaDepartamento/menuPrincipal";
		}
	}

	@ResponseBody
	@RequestMapping(value = "/alocaDisciplinas")
	public String alocaDisciplinas(@RequestBody Map<String, String> lotacoes,
			Model model) {

		try {
			service.alocarDisciplinas(lotacoes);
			model.addAttribute("sucesso", "Lotações registradas com sucesso!");
			return "Alocações registradas com sucesso!";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "Erro ao registrar alocações de disciplinas a departamentos!"
					+ "\n" + exc.getMessage();
		}
	}

}
