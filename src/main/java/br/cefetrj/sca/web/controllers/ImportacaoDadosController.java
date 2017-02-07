package br.cefetrj.sca.web.controllers;

import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.service.ImportacaoDadosService;

@Controller
@RequestMapping("/importacaoDados")
public class ImportacaoDadosController {

	@Autowired
	private ImportacaoDadosService service;

	DescritorImportacaoDados descritor = new DescritorImportacaoDados();

	protected Logger logger = Logger.getLogger(ImportacaoDadosController.class.getName());

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}

	@RequestMapping(value = "/homeImportacaoDados", method = RequestMethod.GET)
	public String homeImportacaoDados(HttpServletRequest request, Model model) {
		try {
			model.addAttribute("descritor", descritor);
			return "/importacaoDados/homeImportacaoDadosView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/homeImportacaoGradeCurricular", method = RequestMethod.GET)
	public String homeImportacaoGradeCurricular(HttpServletRequest request, Model model) {
		try {
			return "/importacaoDados/homeImportacaoGradeCurricularView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/importacaoGradeCurricular", method = RequestMethod.POST)
	public String importacaoGradeCurricular(HttpServletRequest request, Model model,
			@RequestParam("tabGradeCurricularFile") MultipartFile file) {
		try {
			String response = service.importarGradesCurriculares(file);
			model.addAttribute("response", response);
			return "/importacaoDados/homeImportacaoGradeCurricularView";
		} catch (Exception exc) {
			exc.printStackTrace();
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/homeImportacaoHistoricoEscolar", method = RequestMethod.GET)
	public String homeImportacaoHistoricoEscolar(HttpServletRequest request, Model model) {
		try {
			return "/importacaoDados/homeImportacaoHistoricoEscolarView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/importacaoHistoricoEscolar", method = RequestMethod.POST)
	public String importacaoHistoricoEscolar(HttpServletRequest request, Model model,
			@RequestParam("tabHistoricoEscolar") MultipartFile file) {
		try {
			String response = service.importarHistoricosEscolares(file);
			model.addAttribute("response", response);
			return "/importacaoDados/homeImportacaoHistoricoEscolarView";
		} catch (Exception exc) {
			exc.printStackTrace();
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

///

	@RequestMapping(value = "/homeImportacaoAlocacoesProfessoresEmTurmas", method = RequestMethod.GET)
	public String homeImportacaoAlocacoesProfessoresEmTurmas(HttpServletRequest request, Model model) {
		try {
			return "/importacaoDados/homeImportacaoAlocacoesProfessoresEmTurmasView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/importacaoAlocacoesProfessoresEmTurmas", method = RequestMethod.POST)
	public String importacaoAlocacoesProfessoresEmTurmas(HttpServletRequest request, Model model,
			@RequestParam("tabHistoricoEscolar") MultipartFile file) {
		try {
			String response = service.importarAlocacoesProfessoresEmTurmas(file);
			model.addAttribute("response", response);
			return "/importacaoDados/homeImportacaoAlocacoesProfessoresEmTurmasView";
		} catch (Exception exc) {
			exc.printStackTrace();
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
///	
	@RequestMapping(value = "/importacaoTabelaAtividadesComplementares", method = RequestMethod.POST)
	public String importacaoTabelaAtividadesComplementares(HttpServletRequest request, Model model,
			@RequestParam("tabAtividadeComplementarFile") MultipartFile file) {
		try {
			String response = service.importar(file, 6L);
			model.addAttribute("response", response);
			model.addAttribute("descritor", descritor);
			return "/importacaoDados/homeImportacaoDadosView";
		} catch (Exception exc) {
			exc.printStackTrace();
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/importacaoProfessores", method = RequestMethod.POST)
	public String importacaoProfessores(HttpServletRequest request, Model model,
			@RequestParam("professorFile") MultipartFile file) {
		try {
			String response = service.importar(file, 7L);
			model.addAttribute("response", response);
			model.addAttribute("descritor", descritor);
			return "/importacaoDados/homeImportacaoDadosView";
		} catch (Exception exc) {
			exc.printStackTrace();
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

	@RequestMapping(value = "/importacaoDados", method = RequestMethod.POST)
	public String importacaoDados(HttpServletRequest request, Model model,
			@RequestParam("historicoFile") MultipartFile file, Long tipoImportacao) {
		try {
			String response = service.importar(file, tipoImportacao);
			model.addAttribute("response", response);
			model.addAttribute("descritor", descritor);
			return "/importacaoDados/homeImportacaoDadosView";
		} catch (Exception exc) {
			exc.printStackTrace();
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

}
