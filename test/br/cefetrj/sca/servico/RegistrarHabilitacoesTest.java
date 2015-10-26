package br.cefetrj.sca.servico;

import javax.sql.DataSource;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.service.RegistrarHabilitacoesService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "file:src/META-INF/applicationContext.xml" })
//@TransactionConfiguration
//@Transactional(value = "transactionManager")
//public class RegistrarHabilitacoesTest extends
//		AbstractTransactionalJUnit4SpringContextTests {
//
//	@Autowired
//	RegistrarHabilitacoesService service;
//
//	@Override
//	@Autowired
//	@Qualifier(value = "dataSource")
//	public void setDataSource(DataSource dataSource) {
//		super.setDataSource(dataSource);
//	}
//
//	@Test
//	public void testProfessorValido() {
//		Professor professor = service.selecionarProfessor("1506449");
//		Assert.assertEquals(professor.getMatricula(), "1506449");
//	}
//
//	@Test
//	public void testProfessor() {
//		System.out.println("RegistrarHabilitacoesTest.testProfessor()");
//	}
//
//}

public class RegistrarHabilitacoesTest {

}