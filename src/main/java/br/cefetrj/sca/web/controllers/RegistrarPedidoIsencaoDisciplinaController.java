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
import br.cefetrj.sca.service.RegistrarPedidoIsencaoDisciplinasService;

@Controller
@SessionAttributes("login")
@RequestMapping("/registroIsencoes")
public class RegistrarPedidoIsencaoDisciplinaController {

	protected Logger logger = Logger
			.getLogger(RegistrarPedidoIsencaoDisciplinaController.class
					.getName());

	@Autowired
	private RegistrarPedidoIsencaoDisciplinasService service;

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
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();

		try {
			model.addAttribute("dadosAluno",
					service.obterSituacaAluno(matricula));
			model.addAttribute("itensPedidoIsencao", service
					.obterFichaIsencaoDisciplinas(matricula).getItens());
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
			model.addAttribute("dadosAluno",
					service.obterSituacaAluno(matricula));
			model.addAttribute("disciplinas",
					service.getDisciplinasPossiveisParaIsencao(matricula));
			model.addAttribute("matricula", matricula);

			return "/isencaoDisciplina/registroIsencoes/novoItemPedidoIsencaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/registroIsencoes/solicitaNovamenteRegistroIsencoes";
		}
	}

	@RequestMapping(value = "/registraItem", method = RequestMethod.POST)
	public String registraAtividade(HttpSession session,
			@RequestParam String idDisciplina,
			@RequestParam String nomeDisciplinaExterna,
			@RequestParam String notaFinalDisciplinaExterna,
			@RequestParam String cargaHoraria, @RequestParam String observacao,
			@RequestParam MultipartFile file, Model model) throws IOException {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(idDisciplina);
			service.registrarItem(matricula, id, nomeDisciplinaExterna,
					notaFinalDisciplinaExterna, cargaHoraria, observacao, file);
			model.addAttribute("info", "Item do pedido de isenção submetido.");

			return "forward:/registroIsencoes/solicitaNovamenteRegistroIsencoes";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/registroIsencoes/solicitaRegistroAtividade";
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
			model.addAttribute("info",
					"Submissão de registro de atividade complementar cancelada.");

			return "forward:/registroIsencoes/solicitaNovamenteRegistroIsencoes";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/registroIsencoes/solicitaNovamenteRegistroIsencoes";
		}
	}

	@RequestMapping(value = "/solicitaNovamenteRegistroIsencoes")
	public String solicitaNovamenteRegistroIsencoes(HttpSession session,
			Model model) {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("dadosAluno",
					service.obterSituacaAluno(matricula));
			model.addAttribute("itensPedidoIsencao",
					service.obterFichaIsencaoDisciplinas(matricula).getItens());
			model.addAttribute("matricula", matricula);

			return "/isencaoDisciplina/registroIsencoes/apresentaRegistrosView";

		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());

			return "forward:/registroIsencoes/menuPrincipal";
		}
	}

	@RequestMapping(value = "/downloadFile", method = RequestMethod.POST)
	public void downloadFile(HttpSession session, @RequestParam String IdReg,
			HttpServletResponse response) {

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(IdReg);
			Comprovante comprovante = service.getComprovante(matricula, id);
			GerenteArquivos.downloadFile(response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
