package br.cefetrj.sca.dominio.repositories;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class UsuarioRepositorioTest {

	@Autowired
	UsuarioRepositorio usuarioRepositorio;

	@Test
	public void testFindTurmasLecionadasPorProfessorEmPeriodo() {
		assertNotNull("Repositório não definido.", usuarioRepositorio);
	}
}
