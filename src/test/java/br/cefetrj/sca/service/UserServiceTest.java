package br.cefetrj.sca.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class UserServiceTest {
	@Autowired
	private UsuarioService userService;

	@Test
	public void findAllUsers() {
		List<Usuario> users = userService.findAll();
		assertNotNull(users);
		assertTrue(!users.isEmpty());
	}
}
