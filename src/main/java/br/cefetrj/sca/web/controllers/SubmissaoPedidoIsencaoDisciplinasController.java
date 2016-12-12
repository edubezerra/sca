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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.cefetrj.sca.dominio.matriculaforaprazo.Comprovante;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.PedidoIsencaoDisciplinasService;

@Controller
@SessionAttributes("login")
@RequestMapping("/registroIsencoes")
public class SubmissaoPedidoIsencaoDisciplinasController {

	protected Logger logger = Logger.getLogger(SubmissaoPedidoIsencaoDisciplinasController.class.getName());

	@Autowired
	private PedidoIsencaoDisciplinasService service;

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

	@RequestMapping(value = "/registroIsencoes", method = RequestMethod.GET)
	public String solicitaRegistroAtividades(HttpSession session, Model model) {

		System.out.println(".solicitaRegistroAtividades()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("dadosAluno", service.obterSituacaAluno(matricula));
			model.addAttribute("itensPedidoIsencao", service.obterFichaIsencaoDisciplinas(matricula).getItens());
			model.addAttribute("matricula", matricula);
			return "/isencaoDisciplina/registroIsencoes/apresentaRegistrosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/registroIsencoes/menuPrincipal";
		}
	}

	@RequestMapping(value = "/solicitaRegistroItem", method = RequestMethod.POST)
	public String solicitaRegistroItem(HttpSession session, Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("dadosAluno", service.obterSituacaAluno(matricula));
			model.addAttribute("disciplinas", service.getDisciplinasPossiveisParaIsencao(matricula));
			model.addAttribute("matricula", matricula);
			return "/isencaoDisciplina/registroIsencoes/novoItemPedidoIsencaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/registroIsencoes/solicitaNovamenteRegistroIsencoes";
		}
	}

	@RequestMapping(value = "/anexarHistoricoEscolar", method = RequestMethod.POST)
	public @ResponseBody String anexarHistoricoEscolar(@RequestParam("file") MultipartFile file, Model model)
			throws IOException {

		System.out.println(".anexarHistoricoEscolar()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			service.anexarHistoricoEscolar(matricula, file);
			
			String mapAsJson;
			try {
				mapAsJson = new ObjectMapper().writeValueAsString(file.getName());
				return mapAsJson;
			} catch (JsonProcessingException e) {
				return null;
			}
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/registroIsencoes/registroIsencoes";
		}
	}

	@RequestMapping(value = "/registraItem", method = RequestMethod.POST)
	public String registraItem(HttpSession session, @RequestParam String idDisciplina,
			@RequestParam String nomeDisciplinaExterna, @RequestParam String notaFinalDisciplinaExterna,
			@RequestParam String cargaHoraria, @RequestParam String observacao, @RequestParam MultipartFile file,
			Model model) throws IOException {

		System.out.println(".registraItem()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(idDisciplina);
			service.adicionarItemNoPedido(matricula, id, nomeDisciplinaExterna, notaFinalDisciplinaExterna,
					cargaHoraria, observacao, file);
			model.addAttribute("info", "Item do pedido de isenção submetido com sucesso.");
			return "forward:/registroIsencoes/solicitaNovamenteRegistroIsencoes";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/registroIsencoes/solicitaRegistroItem";
		}
	}

	@RequestMapping(value = "/removeRegistroItem", method = RequestMethod.POST)
	public String removeRegistroAtividade(HttpSession session, @RequestParam String idReg, Model model) {

		System.out.println(".removeRegistroAtividade()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(idReg);
			service.removerItemDoPedido(matricula, id);
			model.addAttribute("info", "Submissão de registro de isenção cancelada.");
			return "forward:/registroIsencoes/solicitaNovamenteRegistroIsencoes";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/registroIsencoes/solicitaNovamenteRegistroIsencoes";
		}
	}

	@RequestMapping(value = "/solicitaNovamenteRegistroIsencoes")
	public String solicitaNovamenteRegistroIsencoes(Model model) {

		System.out.println(".solicitaNovamenteRegistroIsencoes()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("dadosAluno", service.obterSituacaAluno(matricula));
			model.addAttribute("itensPedidoIsencao", service.obterFichaIsencaoDisciplinas(matricula).getItens());
			model.addAttribute("matricula", matricula);
			return "/isencaoDisciplina/registroIsencoes/apresentaRegistrosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/registroIsencoes/menuPrincipal";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(@RequestParam String IdReg, HttpServletResponse response) {

		System.out.println(".downloadFile()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(IdReg);
			Comprovante comprovante = service.getComprovanteParaDisciplina(matricula, id);
			GerenteArquivos.downloadFile(response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
