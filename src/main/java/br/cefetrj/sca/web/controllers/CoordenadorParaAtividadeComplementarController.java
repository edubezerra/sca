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
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.CoordenacaoAtividadesComplementaresService;

@Controller
@SessionAttributes("roles")
@RequestMapping("/coordenacaoAtividades")
public class CoordenadorParaAtividadeComplementarController {

	@Autowired
	CoordenacaoAtividadesComplementaresService service;

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
	
	@RequestMapping(value = { "/", "/homeAlocacaoCoordenadorAtividades" }, method = RequestMethod.GET)
	public String paginaInicialAlocacaoCoordenadorAtividades(HttpServletRequest request, HttpSession session, Model model){
		try {
			model.addAttribute("dadosCoordenacaoAtividades", 
					service.listarCoordenadoresAtividadeByCurso());
			
			return "/atividadeComplementar/alocacaoProfessorCoordenador/alocaProfessorCoordenadorView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/coordenacaoAtividades/menuPrincipal";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/alocaCoordenadoresAtividades")
	public String alocaCoordenadoresAtividades(
			@RequestBody Map<String,String> coordenacaoCursoProf,
			Model model){
		
		try {
			service.alocarCoordenadoresAtividadeByCurso(coordenacaoCursoProf);
			model.addAttribute("sucesso", 
					"Coordenadores de atividades complementares registrados com sucesso!");
			return "Coordenadores de atividades complementares registrados com sucesso!";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "Erro ao registrar coordenadores de atividades complementares!" + "\n"+
					exc.getMessage();
		}
	}

}
