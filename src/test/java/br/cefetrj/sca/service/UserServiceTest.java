/**
 * 
 */
package br.cefetrj.sca.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Date;
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

	@Test
	public void findUserById() {
		Usuario user = userService.findUserById(1);
		assertNotNull(user);
	}

	@Test(expected = javax.validation.ConstraintViolationException.class)
	public void createUser() {
		Usuario user = new Usuario(0, "Eduardo Bezerra", "edubezerra",
				"edubezerra", "edubezerra@gmail.com", new Date());
		Usuario savedUser = userService.create(user);
		Usuario newUser = userService.findUserById(savedUser.getId());
		assertEquals("Eduardo", newUser.getNome());
		assertEquals("edubezerra@gmail.com", newUser.getLogin());
	}

}
