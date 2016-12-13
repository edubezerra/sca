package br.cefetrj.sca.web.controllers;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/submissaoIsencoes")
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

	@RequestMapping(value = "/apresentaPedidoIsencaoDisciplinas", method = RequestMethod.GET)
	public String apresentaPedidoIsencaoDisciplinas(HttpSession session, Model model) {

		System.out.println(".solicitaRegistroAtividades()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("fichaIsencaoDisciplinas", service.criarFichaIsencaoDisciplinas(matricula));
			model.addAttribute("itensPedidoIsencao", service.criarFichaIsencaoDisciplinas(matricula).getItens());
			model.addAttribute("matricula", matricula);
			return "/isencaoDisciplina/submissao/apresentaPedidoIsencaoDisciplinasView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/submissaoIsencoes/menuPrincipal";
		}
	}

	@RequestMapping(value = "/solicitaRegistroItem", method = RequestMethod.POST)
	public String solicitaRegistroItem(HttpSession session, Model model) {
		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("fichaIsencaoDisciplinas", service.criarFichaIsencaoDisciplinas(matricula));
			model.addAttribute("disciplinas", service.getDisciplinasPossiveisParaIsencao(matricula));
			model.addAttribute("matricula", matricula);
			return "/isencaoDisciplina/submissao/novoItemPedidoIsencaoView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes";
		}
	}

	@RequestMapping(value = "/anexarHistoricoEscolar", method = RequestMethod.POST)
	public @ResponseBody String anexarHistoricoEscolar(@RequestParam("file") MultipartFile file, Model model) {
		System.out.println(".anexarHistoricoEscolar()");

		try {
			String mapAsJson;
			Usuario usr = UsuarioController.getCurrentUser();
			String matricula = usr.getMatricula();
			try {
				service.anexarHistoricoEscolar(matricula, file);

				mapAsJson = new ObjectMapper().writeValueAsString(file.getName());
				return mapAsJson;
			} catch (Exception exc) {
				mapAsJson = new ObjectMapper().writeValueAsString(exc.getMessage());
				return mapAsJson;
			}
		} catch (JsonProcessingException e) {
			return null;
		}
	}

	@RequestMapping(value = "/adicionarItemNoPedido", method = RequestMethod.POST)
	public String adicionarItemNoPedido(@RequestParam String idDisciplina, @RequestParam String nomeDisciplinaExterna,
			@RequestParam String notaFinalDisciplinaExterna, @RequestParam String cargaHoraria,
			@RequestParam String observacao, @RequestParam MultipartFile file, Model model) throws IOException {

		System.out.println(".adicionarItemNoPedido()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(idDisciplina);
			service.adicionarItemNoPedido(matricula, id, nomeDisciplinaExterna, notaFinalDisciplinaExterna,
					cargaHoraria, observacao, file);
			model.addAttribute("info", "Item do pedido de isenção submetido com sucesso.");
			return "forward:/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/submissaoIsencoes/solicitaRegistroItem";
		}
	}

	@RequestMapping(value = "/removerItemDoPedido", method = RequestMethod.POST)
	public String removerItemDoPedido(@RequestParam String idReg, Model model) {

		System.out.println(".removerItemDoPedido()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Long id = Long.parseLong(idReg);
			service.removerItemDoPedido(matricula, id);
			model.addAttribute("info", "Submissão de registro de isenção cancelada.");
			return "forward:/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes";
		}
	}

	@RequestMapping(value = "/solicitaNovamenteSubmissaoIsencoes")
	public String solicitaNovamenteSubmissaoIsencoes(Model model) {

		System.out.println(".solicitaNovamenteSubmissaoIsencoes()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			model.addAttribute("fichaIsencaoDisciplinas", service.criarFichaIsencaoDisciplinas(matricula));
			model.addAttribute("itensPedidoIsencao", service.criarFichaIsencaoDisciplinas(matricula).getItens());
			model.addAttribute("matricula", matricula);
			return "/isencaoDisciplina/submissao/apresentaPedidoIsencaoDisciplinasView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/submissaoIsencoes/menuPrincipal";
		}
	}

	/**
	 * Realiza o dowload do arquivo contendo o(s) conteúdo(s) programático(s) de
	 * uma ou mais disciplinas externas usadas em um item do pedido de isenção.
	 * Esses conteúdo(s) programático(s) são posteriormente usados como
	 * comprovantes, durante a análise do pedido de isenção.
	 * 
	 * Pode ser que mais de uma disciplina externa seja usada na solicitação de
	 * isenção de uma mesma disciplina interna. Nesse caso, esse arquivo está no
	 * formato ZIP e contém todos os conteúdos programáticos para cada
	 * disciplina externa usada.
	 * 
	 * @param IdReg
	 *            identificador do objeto
	 *            <code>ItemPedidoIsencaoDisciplina</code> correspondente.
	 */
	@RequestMapping(value = "/downloadConteudoProgramatico", method = RequestMethod.POST)
	public void downloadConteudoProgramatico(@RequestParam String IdReg, HttpServletResponse response) {

		System.out.println(".downloadConteudoProgramatico()");

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

	@RequestMapping(value = "/downloadHistoricoEscolar", method = RequestMethod.POST)
	public void downloadHistoricoEscolar(@RequestParam String nomeArquivo, HttpServletResponse response) {

		System.out.println(".downloadHistoricoEscolar()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			Comprovante comprovante = service.getComprovanteHistoricoEscolar(matricula, nomeArquivo);
			GerenteArquivos.downloadFile(response, comprovante);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/removerComprovanteHistoricoEscolar", method = RequestMethod.POST)
	public String removerComprovanteHistoricoEscolar(@RequestParam String nomeArquivo, Model model) {

		System.out.println(".removerItemDoPedido()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matricula = usr.getMatricula();
		try {
			service.removerComprovanteHistoricoEscolar(matricula, nomeArquivo);
			model.addAttribute("info", "Comprovante de histórico escolar removido com sucesso.");
			return "forward:/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes";
		}
	}

	@RequestMapping(value = { "/submeterPedidoParaAnalise-{matricula}" }, method = RequestMethod.GET)
	public String submeterPedidoParaAnalise(@PathVariable String matricula, Model model) {

		System.out.println(".submeterPedidoParaAnalise()");

		Usuario usr = UsuarioController.getCurrentUser();
		String matriculaAlunoLogado = usr.getMatricula();

		try {
			/**
			 * Permite a submissão do pedido de isenção apenas se o aluno
			 * solicitante (da submissão) for o mesmo que montou o pedido.
			 */
			if (matriculaAlunoLogado.equals(matricula)) {
				service.submeterPedidoParaAnalise(matricula);
			}
			return "forward:/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "forward:/submissaoIsencoes/solicitaNovamenteSubmissaoIsencoes";
		}
	}
}
