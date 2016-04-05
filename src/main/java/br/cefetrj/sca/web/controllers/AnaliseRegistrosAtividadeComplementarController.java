package br.cefetrj.sca.web.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.inclusaodisciplina.Comprovante;
import br.cefetrj.sca.service.AnaliseRegistrosAtividadeService;
import br.cefetrj.sca.service.util.RegistrosAtividadeSearchCriteria;
import br.cefetrj.sca.service.util.SolicitaRegistroAtividadesResponse;

@Controller
@SessionAttributes("login")
@RequestMapping("/analiseAtividades")
public class AnaliseRegistrosAtividadeComplementarController {
	
	@Autowired
	AnaliseRegistrosAtividadeService service;
	
	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}
	
	@RequestMapping(value = "/menuPrincipal")
	public String menuPrincipal(HttpSession session, Model model) {
		String matricula = (String) session.getAttribute("login");
		if (matricula != null) {
			return "/menuPrincipalView";
		} else {
			return "/homeView";
		}
	}
	
	@RequestMapping(value = "/homeAnalise", method = RequestMethod.GET)
	public String paginaInicialAnalise(HttpServletRequest request, HttpSession session, Model model){
		try {
			String matricula = (String) session.getAttribute("login");
			service.homeAnaliseAtividades(matricula, model);		
			return "/analiseAtividades/analiseRegistrosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/analiseAtividades/menuPrincipal";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/listarAtividades")
	public SolicitaRegistroAtividadesResponse listarAtividades(
			@RequestBody RegistrosAtividadeSearchCriteria search,
			Model model) {
		
		SolicitaRegistroAtividadesResponse registrosAtiv = null;
		
		try {
			registrosAtiv = service.listarRegistrosAtividade(search.getSiglaCurso(),search.getNumeroVersao(),search.getStatus());
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
		}
		return registrosAtiv;
	}
		
	@RequestMapping(value = "/defineStatusAtividade", method = RequestMethod.POST)
	public String atualizaStatusRegistro(HttpSession session, Model model,
			@RequestParam(value = "status") String status, 
			@RequestParam Long idRegistro,
			@RequestParam String matriculaAluno,
			@ModelAttribute("siglaCurso") String siglaCurso,
			@ModelAttribute("numeroVersao") String numeroVersao,
			@ModelAttribute("status") String statusBusca) {
		try {
			service.atualizaStatusRegistro(matriculaAluno,idRegistro,status);
			model.addAttribute("registrosAtiv", 
					service.listarRegistrosAtividade(siglaCurso, numeroVersao, statusBusca));
			return "forward:/analiseAtividades/listarAtividades";		
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/analiseAtividades/listarAtividades";
		}
	}
	
	@RequestMapping(value = "/solicitaNovamenteHomeAnalise")
	public String solicitaNovamenteHomeAnalise(HttpServletRequest request, HttpSession session, Model model){
		try {
			String matricula = (String) session.getAttribute("login");
			service.homeAnaliseAtividades(matricula, model);		
			return "/analiseAtividades/analiseRegistrosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/analiseAtividades/menuPrincipal";
		}
	}
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(HttpSession session,
			@RequestParam String IdReg, HttpServletResponse response) {
		
		String matricula = (String) session.getAttribute("login");
		try {
			Long id = Long.parseLong(IdReg);
			Comprovante comprovante = service.getComprovante(matricula,id);
			GerenteArquivos.downloadFile(response,comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		
}
