/**
 * 
 */
package br.cefetrj.sca.config;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.User;
import br.cefetrj.sca.service.UserService;
import br.cefetrj.sca.web.config.SecurityUser;

@Component
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user = userService.findUserByEmail(userName);
		if (user == null) {
			throw new UsernameNotFoundException("Usuário " + userName
					+ " não encontrado.");
		}
		return new SecurityUser(user);
	}

}
