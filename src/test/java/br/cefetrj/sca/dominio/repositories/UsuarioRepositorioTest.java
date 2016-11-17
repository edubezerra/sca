package br.cefetrj.sca.dominio.repositories;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class UsuarioRepositorioTest {

	@Autowired
	UsuarioRepositorio usuarioRepositorio;

	@Test
	public void testFindUsuarioByLogin() {
		assertNotNull("Repositório não definido.", usuarioRepositorio);
		Usuario usuario = usuarioRepositorio.findUsuarioByLogin("1223216BCC");
		assertNotNull(usuario);
	}
}
