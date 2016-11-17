/**
 * 
 */
package br.cefetrj.sca.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import br.cefetrj.sca.infra.auth.CustomAuthenticationProvider;

@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(prePostEnabled = true)
// @ImportResource("classpath:applicationContext-security.xml")
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomAuthenticationProvider authProvider;

	@Override
	protected void configure(AuthenticationManagerBuilder registry)
			throws Exception {
		registry.authenticationProvider(authProvider);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**"); // #3
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().authorizeRequests()
				.antMatchers("/login", "/login/form**", "/register", "/logout", "/monografias", "/monografias/**")
				.permitAll()
				// #4
				.antMatchers("/admin", "/admin/**", "/monografias/blacklist**").hasRole("ADMIN")
				// #5
				.antMatchers("/monografias/minhas**").hasRole("ALUNO")
				// #6
				.anyRequest().authenticated()
				// 7
				.and().formLogin()
				// #8
				.loginPage("/login/form")
				// #9
				.loginProcessingUrl("/login").failureUrl("/login/form?error")
				.permitAll(); // #5
	}
}
