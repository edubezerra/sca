package br.cefetrj.sca.web.controllers;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
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
public class AppController {

	@Autowired
	UsuarioService userService;
	
	@Autowired
	UserProfileService userProfileService;
	
	
	@Autowired
	MessageSource messageSource;

	/**
	 * This method will list all existing users.
	 */
	@RequestMapping(value = { "/", "/list" }, method = RequestMethod.GET)
	public String listUsers(ModelMap model) {

		List<Usuario> users = userService.findAllUsers();
		model.addAttribute("users", users);
		return "/usuarios/userslist";
	}

	/**
	 * This method will provide the medium to add a new user.
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
	public String saveUser(@Valid Usuario user, BindingResult result,
			ModelMap model) {

		if (result.hasErrors()) {
			return "/usuarios/registration";
		}

//		/*
//		 * Preferred way to achieve uniqueness of field [sso] should be implementing custom @Unique annotation 
//		 * and applying it on field [sso] of Model class [User].
//		 * 
//		 * Below mentioned peace of code [if block] is to demonstrate that you can fill custom errors outside the validation
//		 * framework as well while still using internationalized messages.
//		 * 
//		 */
//		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
//			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
//		    result.addError(ssoError);
//			return "registration";
//		}
		
		userService.saveUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " registered successfully");
		//return "success";
		return "/usuarios/registrationsuccess";
	}


	/**
	 * This method will provide the medium to update an existing user.
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.GET)
	public String editUser(@PathVariable String ssoId, ModelMap model) {
		Usuario user = userService.findById(Integer.parseInt(ssoId));
		model.addAttribute("user", user);
		model.addAttribute("edit", true);
		return "/usuarios/registration";
	}
	
	/**
	 * This method will be called on form submission, handling POST request for
	 * updating user in database. It also validates the user input
	 */
	@RequestMapping(value = { "/edit-user-{ssoId}" }, method = RequestMethod.POST)
	public String updateUser(@Valid Usuario user, BindingResult result,
			ModelMap model, @PathVariable String ssoId) {

		if (result.hasErrors()) {
			return "/usuarios/registration";
		}

		/*//Uncomment below 'if block' if you WANT TO ALLOW UPDATING SSO_ID in UI which is a unique key to a User.
		if(!userService.isUserSSOUnique(user.getId(), user.getSsoId())){
			FieldError ssoError =new FieldError("user","ssoId",messageSource.getMessage("non.unique.ssoId", new String[]{user.getSsoId()}, Locale.getDefault()));
		    result.addError(ssoError);
			return "registration";
		}*/


		userService.updateUser(user);

		model.addAttribute("success", "User " + user.getFirstName() + " "+ user.getLastName() + " updated successfully");
		return "/usuarios/registrationsuccess";
	}

	
	/**
	 * This method will delete an user by it's SSOID value.
	 */
	@RequestMapping(value = { "/delete-user-{ssoId}" }, method = RequestMethod.GET)
	public String deleteUser(@PathVariable String ssoId) {
		System.err.println("AppController.deleteUser()");
//		userService.deleteUserBySSO(ssoId);
		return "redirect:/list";
	}
	

	/**
	 * This method will provide UserProfile list to views
	 */
	@ModelAttribute("roles")
	public List<PerfilUsuario> initializeProfiles() {
		return userProfileService.findAll();
	}

}
