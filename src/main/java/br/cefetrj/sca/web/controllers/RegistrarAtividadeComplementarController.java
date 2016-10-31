package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.RegistrarAtividadesComplementaresService;

@Controller
@SessionAttributes("login")
@RequestMapping("/registroAtividades")
public class RegistrarAtividadeComplementarController {

	protected Logger logger = Logger.getLogger(RegistrarAtividadeComplementarController.class
			.getName());

	@Autowired
	private RegistrarAtividadesComplementaresService service;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/login";
	}
	
	@RequestMapping(value = "/menuPrincipal")
	public String menuPrincipal(HttpSession session,
			Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		if (matricula != null) {
			return "/menuPrincipalView";
		} else {
			return "/login";
		}
	}

	@RequestMapping(value = "/registroAtividades", method = RequestMethod.GET)
	public String solicitaRegistroAtividades(HttpSession session, Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();

		try {
			model.addAttribute("dadosAluno",
					service.obterSituacaAluno(matricula));
			model.addAttribute("registrosAtiv",
					service.obterRegistrosAtividade(matricula));
			model.addAttribute("situacaoAtiv",
					service.obterSituacaoAtividades(matricula));
			model.addAttribute("categorias", 
					service.obterCategoriasAtividade(matricula));
			model.addAttribute("matricula", matricula);
			return "/atividadeComplementar/registroAtividades/apresentaRegistrosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/registroAtividades/menuPrincipal";
		}
	}

	@RequestMapping(value = "/solicitaRegistroAtividade", method = RequestMethod.POST)
	public String solicitaRegistroAtividade(HttpSession session,
			@RequestParam String idAtiv, Model model) {
		
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("dadosAluno",
					service.obterSituacaAluno(matricula));
			Long id = Long.parseLong(idAtiv);
			model.addAttribute("dadosAtiv",
					service.obterSituacaoAtividade(matricula, id));
			model.addAttribute("idAtiv", idAtiv);
			model.addAttribute("matricula", matricula);
						
			return "/atividadeComplementar/registroAtividades/novoRegistroView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/registroAtividades/solicitaNovamenteRegistroAtividades";
		}
	}

	@RequestMapping(value = "/registraAtividade", method = RequestMethod.POST)
	public String registraAtividade(HttpSession session,
			@RequestParam String idAtiv,
			@RequestParam String descricao,			
			@RequestParam String cargaHoraria,
			@RequestParam MultipartFile file,
			Model model)  throws IOException {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(idAtiv);
			service.registraAtividade(matricula, id, descricao, cargaHoraria, file);
			model.addAttribute("info", "Registro de atividade complementar submetido.");
						
			return "forward:/registroAtividades/solicitaNovamenteRegistroAtividades";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/registroAtividades/solicitaRegistroAtividade";
		}
		
	}
	
	@RequestMapping(value = "/removeRegistroAtividade", method = RequestMethod.POST)
	public String removeRegistroAtividade(HttpSession session,
			@RequestParam String idReg, Model model) {
		
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(idReg);
			service.removeRegistroAtividade(matricula, id);
			model.addAttribute("info", "Submissão de registro de atividade complementar cancelada.");
						
			return "forward:/registroAtividades/solicitaNovamenteRegistroAtividades";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/registroAtividades/solicitaNovamenteRegistroAtividades";
		}
	}
	
	@RequestMapping(value = "/solicitaNovamenteRegistroAtividades")
	public String solicitaNovamenteRegistroAtividades(HttpSession session,
			Model model) {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("dadosAluno",
					service.obterSituacaAluno(matricula));
			model.addAttribute("registrosAtiv",
					service.obterRegistrosAtividade(matricula));
			model.addAttribute("situacaoAtiv",
					service.obterSituacaoAtividades(matricula));
			model.addAttribute("categorias", 
					service.obterCategoriasAtividade(matricula));
			model.addAttribute("matricula", matricula);
			
			return "/atividadeComplementar/registroAtividades/apresentaRegistrosView";
			
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/registroAtividades/menuPrincipal";
		}
	}
	
	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(HttpSession session,
			@RequestParam String IdReg, HttpServletResponse response) {
		
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(IdReg);
			Comprovante comprovante = service.getComprovante(matricula,id);
			GerenteArquivos.downloadFile(response,comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
