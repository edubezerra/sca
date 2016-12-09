package br.cefetrj.sca.web.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.AnaliseRegistrosAtividadeService;
import br.cefetrj.sca.service.util.AtualizaStatusAtividadeSearchCriteria;
import br.cefetrj.sca.service.util.RegistrosAtividadeSearchCriteria;
import br.cefetrj.sca.service.util.SolicitaRegistroAtividadesResponse;
import br.cefetrj.sca.service.util.VersoesCursoSearchCriteria;

@Controller
@SessionAttributes("login")
@RequestMapping("/analiseIsencoes")
public class AnalisePedidoIsencaoDisciplinaControllerTEMP {
	
	@Autowired
	AnaliseRegistrosAtividadeService service;
	
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
	
	@RequestMapping(value = "/homeAnalise", method = RequestMethod.GET)
	public String paginaInicialAnalise(HttpServletRequest request, HttpSession session, Model model){
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matricula = usr.getMatricula();
			model.addAttribute("dadosAnaliseAtividades", 
					service.homeAnaliseAtividades(matricula));				
			return "/isencaoDisciplina/analiseIsencoes/analiseRegistrosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/analiseIsencoes/menuPrincipal";
		}
	}
	
	@ResponseBody
	@RequestMapping(value = "/obterVersoesCurso")
	public List<String> obterVersoesCurso(
			@RequestBody VersoesCursoSearchCriteria search,
			Model model) {
		
		List<String> versoesCurso = null;
		
		try {
			versoesCurso = service.obterVersoesCurso(search.getSiglaCurso());
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
		}
		return versoesCurso;
	}
	
	@ResponseBody
	@RequestMapping(value = "/listarAtividades")
	public SolicitaRegistroAtividadesResponse listarAtividades(
			@RequestBody RegistrosAtividadeSearchCriteria search,
			Model model) {
		
		SolicitaRegistroAtividadesResponse registrosAtiv = null;
		
		try {
			registrosAtiv = service.listarRegistrosAtividade(search.getMatriculaProf(),search.getSiglaCurso(),search.getNumeroVersao(),search.getStatus(),
					search.getStartDate(),search.getEndDate());
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
		}
		return registrosAtiv;
	}
	
	@ResponseBody
	@RequestMapping(value = "/defineStatusAtividade")
	public SolicitaRegistroAtividadesResponse atualizaStatusRegistro(
			@RequestBody AtualizaStatusAtividadeSearchCriteria search,
			Model model){
		
		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaProfessor = usr.getMatricula();
		
		SolicitaRegistroAtividadesResponse registrosAtiv = null;
		
		try {
			Long idRegistro = Long.parseLong(search.getIdRegistro());			
			service.atualizaStatusRegistro(matriculaProfessor,search.getMatriculaAluno(),
					idRegistro,search.getNovoStatus(), search.getJustificativa());
			registrosAtiv = service.listarRegistrosAtividade(search.getMatriculaProf(),search.getSiglaCurso(),search.getNumeroVersao(),search.getStatus(),
					search.getStartDate(),search.getEndDate());
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
		}
		return registrosAtiv;
	}
	
	@RequestMapping(value = "/solicitaNovamenteHomeAnalise")
	public String solicitaNovamenteHomeAnalise(HttpServletRequest request, HttpSession session, Model model){
		try {
			Usuario usr = UsuarioController.getCurrentUser();
			String matricula = usr.getMatricula();
			model.addAttribute("dadosAnaliseAtividades", 
					service.homeAnaliseAtividades(matricula));
			return "/isencaoDisciplina/analiseIsencoes/analiseRegistrosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/analiseIsencoes/menuPrincipal";
		}
	}
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(HttpSession session,
			@RequestParam String IdReg, 
			@RequestParam String matriculaAluno, 
			HttpServletResponse response) {
		
		try {
			Long id = Long.parseLong(IdReg);
			Comprovante comprovante = service.getComprovante(matriculaAluno,id);
			GerenteArquivos.downloadFile(response,comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		
}
