package br.cefetrj.sca.servico;

import static org.junit.Assert.*;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.service.RegistrarHabilitacoesService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/META-INF/applicationContext.xml" })
@TransactionConfiguration
@Transactional(value = "transactionManager")
public class RegistrarHabilitacoesTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	RegistrarHabilitacoesService service;

	@Override
	@Autowired
	@Qualifier(value = "dataSource")
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Test
	public void testProfessorValido() {
		Professor professor = service.selecionarProfessor("1506449");
		assertEquals(professor.getMatricula(), "1506449");
	}

	@Test
	public void testProfessor() {
		System.out.println("RegistrarHabilitacoesTest.testProfessor()");
	}

}
