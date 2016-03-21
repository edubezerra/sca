/**
 * 
 */
package br.cefetrj.sca.web.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.cefetrj.sca.dominio.Role;
import br.cefetrj.sca.dominio.User;

public class SecurityUser extends User implements UserDetails {

	private static final long serialVersionUID = 1L;

	public SecurityUser(User user) {
		if (user != null) {
			this.setId(user.getId());
			this.setName(user.getName());
			this.setLogin(user.getLogin());
			this.setPassword(user.getPassword());
			this.setDob(user.getDob());
			this.setRoles(user.getRoles());
		}
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		Collection<GrantedAuthority> authorities = new ArrayList<>();
		Set<Role> userRoles = this.getRoles();

		if (userRoles != null) {
			for (Role role : userRoles) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(
						role.getRoleName());
				authorities.add(authority);
			}
		}
		return authorities;
	}

	@Override
	public String getPassword() {
		return super.getPassword();
	}

	@Override
	public String getUsername() {
		return super.getLogin();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

}
