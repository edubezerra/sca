package br.cefetrj.sca.servico;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidade;
import br.cefetrj.sca.service.FornecerGradeDisponibilidadeService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/applicationContext.xml" })
@TransactionConfiguration
@Transactional
public class FornecerGradeDisponibilidadeServiceTest extends
		AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	protected FornecerGradeDisponibilidadeService servico;

	@Test
	public void testValidarMatricula() {
		assertNotNull("Serviço não iniciado.", servico);
		FichaDisponibilidade ficha = servico.validarProfessor("1506449");
		assertEquals(ficha.getMatriculaProfessor(), "1506449");
	}
	
	@Test
	public void testAdicionarDisciplina() {
		servico.adicionarDisciplina();	
	}
	
}