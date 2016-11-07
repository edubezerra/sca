package br.cefetrj.sca.web.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;
import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.UserProfileService;
import br.cefetrj.sca.service.UsuarioService;

@Controller
@RequestMapping("/usuarios")
@SessionAttributes("roles")
public class UsuarioController {

	@Autowired
	private static UsuarioService usuarioService;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	MessageSource messageSource;

	@Autowired
	public void setUserService(UsuarioService userService) {
		UsuarioController.usuarioService = userService;
	}

	public static Usuario getCurrentUser() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String login = authentication.getName();
		Usuario usuario = usuarioService.findUsuarioByLogin(login);
		return usuario;
	}

	/**
	 * Esse método lista todos os usuários.
	 */
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<Usuario> users = usuarioService.findAll();
		model.addAttribute("users", users);

		return "/usuarios/userslist";
	}

	/**
	 * Esse método fornece um meio de adicionar um novo usuário.
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.GET)
	public String newUser(ModelMap model) {
		Usuario user = new Usuario();
		model.addAttribute("user", user);
		model.addAttribute("edit", false);
		return "/usuarios/registration";
	}

	/**
	 * This method will be called on form submission, handling POST request for
	 * saving user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/newuser" }, method = RequestMethod.POST)
	public String saveUser(@Valid Usuario user, BindingResult result, ModelMap model) {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "/usuarios/registration";
		}

		if (!usuarioService.isLoginJaExistente(user.getId(), user.getLogin())) {
			FieldError loginError = new FieldError("user", "login", messageSource.getMessage("non.unique.login",
					new String[] { user.getLogin() }, Locale.getDefault()));
			result.addError(loginError);
			return "/usuarios/registration";
		}

		usuarioService.adicionarUsuario(user);

		model.addAttribute("success", "Usuário " + user.getNome() + " registrado com sucesso");

		return "/usuarios/registrationsuccess";
	}

	/**
	 * Esse método fornece um meio de atualizar um usuário.
	 */
	@RequestMapping(value = { "/edit-user-{login}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String login, ModelMap model) {
		Usuario user = usuarioService.findUsuarioByLogin(login);
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		return "/usuarios/registration";
	}

	/**
	 * Este método, chamado na submissão do form, manipula a requisição POST
	 * para atualizar um usuário. Ele também valida os dados fornecidos.
	 */
	@RequestMapping(value = { "/edit-user-{login}" }, method = RequestMethod.POST)
	public String updateUser(@Valid Usuario user, BindingResult result, ModelMap model, @PathVariable String login) {

		if (result.hasErrors()) {
			model.addAttribute("user", user);
			return "/usuarios/registration";
		}

		usuarioService.atualizarUsuario(user);

		model.addAttribute("success", "Usuário " + user.getNome() + " atualizado com sucesso");
		return "/usuarios/registrationsuccess";
	}

	/**
	 * Este método remove um usuário identificado pelo seu login.
	 */
	@RequestMapping(value = { "/delete-user-{login}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String login) {
		usuarioService.deleteUser(login);
		return "redirect:/usuarios/list";
	}

	/**
	 * Este método fornece uma lista de objetos <code>PerfilUsuario</code>
	 */
	@ModelAttribute("roles")
	public List<PerfilUsuario> initializeProfiles() {
		return userProfileService.findAll();
	}

}
