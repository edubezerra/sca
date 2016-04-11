/**
 * 
 */
package br.cefetrj.sca.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.usuarios.Usuario;
import br.cefetrj.sca.service.UsuarioService;
import br.cefetrj.sca.web.config.SecurityUser;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UsuarioService userService;

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		Usuario user = userService.findUserByLogin(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Usuário " + userName + " não encontrado.");
		}
		return new SecurityUser(user);
	}

}
