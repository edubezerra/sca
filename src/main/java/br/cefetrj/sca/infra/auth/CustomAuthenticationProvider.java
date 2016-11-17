package br.cefetrj.sca.infra.auth;

import java.util.*;

import br.cefetrj.sca.dominio.repositories.PerfilUsuarioRepositorio;
import br.cefetrj.sca.dominio.usuarios.TipoPerfilUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
	@Qualifier("mockAuth")
	EstrategiaAutenticacao autenticador;

	@Autowired
	UsuarioRepositorio usuarioRepositorio;

	@Autowired
	PerfilUsuarioRepositorio perfilUsuarioRepositorio;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		
		System.out.println("CustomAuthenticationProvider.authenticate()");
		
		String login = authentication.getName();
		String password = authentication.getCredentials().toString();

		Usuario usuario = usuarioRepositorio.findUsuarioByLogin(login);


//		/**
//		 * Procura novamente, devido à existência de dois formatos para o login
//		 * no Moodle: um usuário com login 999.999.999/99 pode ter login
//		 * 99999999999 ou 999999999-99.
//		 * 
//		 * TODO: remover esse teste quando migrar para a autenticação pelo SIE.
//		 */
//		if (usuario == null) {
//			String loginModificado = login.substring(0, login.length() - 2)
//					+ "-" + login.substring(login.length() - 2);
//			usuario = usuarioRepositorio.findUsuarioByLogin(loginModificado);
//		}

		if (usuario != null) {
			if (deveAutenticarNoSistemaExterno(login, password)) {
				return new UsernamePasswordAuthenticationToken(login, password,
						getAuthorities(usuario));
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private Collection<? extends GrantedAuthority> getAuthorities(
			Usuario usuario) {
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
	private boolean deveAutenticarNoSistemaExterno(String login, String password) {
		try {
			return autenticador.getRemoteLoginResponse(login, password).equals(
					login);
		} catch (RuntimeException ex) {
			return false;
		}
	}

	public static void main(String[] args) {
		String login = "02848884789";
		String loginModificado = login.substring(0, login.length() - 2) + "-"
				+ login.substring(login.length() - 2);
		System.out.println(loginModificado);
	}
}
