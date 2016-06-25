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
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();

		if (deveAutenticarNoSistemaExterno(name, password)) {
			return new UsernamePasswordAuthenticationToken(name, password,
					getAuthorities(name));
		} else {
			return null;
		}
	}

	private Collection<? extends GrantedAuthority> getAuthorities(String name) {
		Usuario usuario = usuarioRepositorio.findUsuarioByLogin(name);
		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Set<PerfilUsuario> userRoles = usuario.getUserProfiles();

		if (userRoles != null) {
			for (PerfilUsuario role : userRoles) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
						role.getType());
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
	private boolean deveAutenticarNoSistemaExterno(String name, String password) {
		return autenticador.getRemoteLoginResponse(name, password).equals(name);
	}
}