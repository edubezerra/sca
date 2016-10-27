package br.cefetrj.sca.dominio.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Departamento;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class DepartamentoRepositorioTest {

	@Autowired
	DepartamentoRepositorio departamentoRepositorio;

	@Test
	public void testFindDepartamentoBySigla() {

		assertNotNull("Repositório não definido.", departamentoRepositorio);

		String sigla = "DEPIN";

		Departamento depto = departamentoRepositorio
				.findDepartamentoBySigla(sigla);

		assertNotNull(depto);

		assertEquals("DEPIN", depto.getSigla());
	}

	@Test
	public void testFindDepartamentoByProfessor() {

		assertNotNull("Repositório não definido.", departamentoRepositorio);

		String matriculaProfessor = "1506449";

		Departamento depto = departamentoRepositorio
				.findDepartamentoByProfessor(matriculaProfessor);

		assertNotNull(depto);

		assertEquals("DEPIN", depto.getSigla());
	}
}
