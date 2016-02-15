package br.cefetrj.sca.apresentacao;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

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
@RequestMapping("/")
public class HomeController {

	@Autowired
	private AutenticacaoService authService;

	@RequestMapping(value = "/{*}", method = RequestMethod.GET)
	public String get(Model model) {
		model.addAttribute("error", "Erro: página não encontrada.");

		return "homeView";
	}

	@RequestMapping(value = "/")
	public String home(Locale locale, Model model) {
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.LONG, locale);

		String formattedDate = dateFormat.format(date);

		model.addAttribute("info", formattedDate);

		return "homeView";
	}

	@RequestMapping(value = "/menuPrincipal", method = RequestMethod.POST)
	public String menuPrincipal(HttpSession session, @RequestParam String matricula,
			@RequestParam String senha, Model model) {

		try {
			authService.autentica(matricula, senha);
			session.setAttribute("matricula", matricula);
			session.setAttribute("login", matricula);
			PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
					.getInstance();
			model.addAttribute("periodoLetivo",
					periodoAvaliacao.getPeriodoLetivo());
			return "/menuPrincipalView";
		} catch (Exception exc) {
			model.addAttribute("error", exc.getMessage());
			return "/homeView";
		}
	}

}
