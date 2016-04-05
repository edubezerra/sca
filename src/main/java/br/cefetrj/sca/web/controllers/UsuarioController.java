package br.cefetrj.sca.web.controllers;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
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
	UsuarioService userService;

	@Autowired
	UserProfileService userProfileService;

	@Autowired
	MessageSource messageSource;

	/**
	 * Esse método lista todos os usuários.
	 */
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<Usuario> users = userService.findAll();
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
			return "/usuarios/registration";
		}

		userService.saveUser(user);

		model.addAttribute("success", "Usuário " + user.getNome() + " registrado com sucesso");

		return "/usuarios/registrationsuccess";
	}

	/**
	 * Esse método fornece um meio de atualizar um usuário.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssoId, ModelMap model) {
		Usuario user = userService.findById(Integer.parseInt(ssoId));
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		return "/usuarios/registration";
	}

	/**
	 * Este método, chamado na submissão do form, manipula a requisição POST
	 * para atualizar um usuário. Ele também valida os dados fornecidos.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
	public String updateUser(@Valid Usuario user, BindingResult result, ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {
			return "/usuarios/registration";
		}

		userService.updateUser(user);

		model.addAttribute("success", "Usuário " + user.getNome() + " atualizado com sucesso");
		return "/usuarios/registrationsuccess";
	}

	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssoId) {
		System.err.println("AppController.deleteUser()");
		// userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}

	/**
	 * Este método fornece uma lista de objetos <code>PerfilUsuario</code>
	 */
	@ModelAttribute("roles")
	public List<PerfilUsuario> initializeProfiles() {
		return userProfileService.findAll();
	}

}
