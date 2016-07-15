package br.cefetrj.sca.infra.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.repositories.UsuarioRepositorio;
import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	AutenticadorMoodle autenticador;

	@Autowired
	UsuarioRepositorio usuarioRepositorio;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();

		Usuario usuario = usuarioRepositorio.findUsuarioByLogin(name);

		if (usuario != null) {
			if (deveAutenticarNoSistemaExterno(name, password)) {
				return new UsernamePasswordAuthenticationToken(name, password, getAuthorities(usuario));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
		Collection<GrantedAuthority> authorities = new ArrayList<>();

		Set<PerfilUsuario> userRoles = usuario.getUserProfiles();

		if (userRoles != null) {
			for (PerfilUsuario role : userRoles) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getType());
				authorities.add(authority);
			}
		}

		return authorities;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}

	/**
	 * Usa as credenciais para autenticar usuário no sistema externo.
	 */
	private boolean deveAutenticarNoSistemaExterno(String login, String password) {
		// return true;
		try {
			boolean primeiraTentativa = autenticador.getRemoteLoginResponse(login, password).equals(login);
			if (!primeiraTentativa) {
				String loginModificado = login.substring(0, login.length() - 2) + "-" + login.substring(login.length() - 2);
				return autenticador.getRemoteLoginResponse(loginModificado, password).equals(loginModificado);
			} else {
				return true;
			}
		} catch (RuntimeException ex) {
			return false;
		}
	}

	public static void main(String[] args) {
		String login = "02848884789";
		String loginModificado = login.substring(0, login.length() - 2) + "-" + login.substring(login.length() - 2);
		System.out.println(loginModificado);
	}
}
