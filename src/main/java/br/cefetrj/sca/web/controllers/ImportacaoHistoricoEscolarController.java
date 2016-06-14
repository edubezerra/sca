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

import br.cefetrj.sca.service.ImportacaoHistoricosEscolaresService;

@Controller
@RequestMapping("/importacaoHistoricoEscolar")
public class ImportacaoHistoricoEscolarController {
	
	@Autowired
	private ImportacaoHistoricosEscolaresService service;

	protected Logger logger = Logger
			.getLogger(ImportacaoHistoricoEscolarController.class.getName());
	
	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");
		return "/homeView";
	}
	
	@RequestMapping(value = "/homeImportacaoHistoricoEscolar", method = RequestMethod.GET)
	public String homeImportacaoHistoricoEscolar(HttpServletRequest request, Model model) {
         try {
        	 return "/importacaoHistoricoEscolar/homeImportacaoHistoricoEscolarView";
         }catch(Exception exc){
        	 model.addAttribute("error", exc.getMessage());
 			 return "/homeView";
         }
	
	}
	
	@RequestMapping(value = "/importacaoHistoricoEscolar", method = RequestMethod.POST)
	public String importacaoHistoricoEscolar(HttpServletRequest request, Model model ,
			@RequestParam("historicoFile") MultipartFile file) {
         try {
        	 
        	 String response = service.importaHistoricoEscolar(file);
        	 
        	 model.addAttribute("response", response);
        	 
        	 return "/importacaoHistoricoEscolar/homeImportacaoHistoricoEscolarView";
        	 
         }catch(Exception exc){
        	 exc.printStackTrace();
        	 
        	 model.addAttribute("error", exc.getMessage());
 			 return "/homeView";
        	 
         }
		
	}
}
