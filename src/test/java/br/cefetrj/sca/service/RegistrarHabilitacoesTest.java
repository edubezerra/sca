package br.cefetrj.sca.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Professor;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional(value = "transactionManager")
public class RegistrarHabilitacoesTest {

	@Autowired
	RegistrarHabilitacoesService service;

	@Test
	public void testProfessorValido() {
		Professor professor = service.selecionarProfessor("1506449");
		assertEquals(professor.getMatricula(), "1506449");
	}
}
