/**
 * 
 */
package br.cefetrj.sca.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.UsuarioService;
import br.cefetrj.sca.web.config.SecurityUser;

@Controller
public class UserController {
	private static UsuarioService userService;

	@Autowired
	public void setUserService(UsuarioService userService) {
		UserController.userService = userService;
	}

	public static Usuario getCurrentUser() {
		Object principal = SecurityContextHolder.getContext()
				.getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			String email = ((UserDetails) principal).getUsername();
			Usuario loginUser = userService.findUserByEmail(email);
			return new SecurityUser(loginUser);
		}

		return null;
	}
}
