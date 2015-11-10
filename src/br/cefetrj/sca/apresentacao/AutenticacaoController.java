package br.cefetrj.sca.apresentacao;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.cefetrj.sca.service.MockAutenticacaoService;

@Controller
@RequestMapping("/autenticacao")
public class AutenticacaoController {
	
	@Autowired
	private MockAutenticacaoService authService;

	@RequestMapping(value = "/menuPrincipal", method = RequestMethod.POST)
	public String menuPrincipal(HttpSession session, @RequestParam String cpf,
			@RequestParam String senha, Model model) {
		try {
			//authService.autentica(cpf, senha);
			return authService.autenticaUsuario(cpf, senha, session, model);
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}
	
}
