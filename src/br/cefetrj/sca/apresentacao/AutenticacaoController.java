package br.cefetrj.sca.apresentacao;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.service.AutenticacaoService;

@Controller
@RequestMapping("/autenticacao")
public class AutenticacaoController {

	@Autowired
	private AutenticacaoService authService;

	@RequestMapping(value = "/menuPrincipal", method = RequestMethod.POST)
	public String menuPrincipal(HttpSession session, @RequestParam String login, @RequestParam String senha,
			Model model) {
		try {
			if (authService.autenticaUsuario(login, senha)) {
				PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas.getInstance();
				model.addAttribute("periodoLetivo", periodoAvaliacao.getSemestreLetivo());
				if (authService.isAluno(login)) {
					session.setAttribute("login", login);
					return "/menuPrincipalView";
				} else if (authService.isProfessor(login)) {
					session.setAttribute("login", login);
					return "/menuPrincipalView";
				} else {
					String erro = "Perfil de usuário não reconhecido. "
							+ "Entre em contato com o administrador do sistema.";
					model.addAttribute("login", erro);
					return "/homeView";
				}
			} else {
				String erro = "Nome de usuário e/ou senha inválidos.";
				model.addAttribute("error", erro);
				return "/homeView";
			}
		} catch (Exception exc) {
			String erro = "Nome de usuário e/ou senha inválidos.";
			model.addAttribute("error", erro);
			return "/homeView";
		}
	}
}
