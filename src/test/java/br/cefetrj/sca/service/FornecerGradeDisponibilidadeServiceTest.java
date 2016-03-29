package br.cefetrj.sca.service;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@TransactionConfiguration
@Transactional
public class FornecerGradeDisponibilidadeServiceTest {

	@Autowired
	protected FornecerGradeDisponibilidadeService servico;

	@Test
	public void testValidarProfessorMatriculaInvalida() {
		assertNotNull("Serviço não iniciado.", servico);
		FichaDisponibilidade ficha = servico.validarProfessor("121212");
		assertNull(ficha);
	}

	@Test
	public void testValidarProfessorMatriculaValida() {
		assertNotNull("Serviço não iniciado.", servico);
		FichaDisponibilidade ficha = servico.validarProfessor("1506449");
		assertEquals(ficha.getMatriculaProfessor(), "1506449");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAdicionarMesmaDisciplina() {
		FichaDisponibilidade ficha = servico.validarProfessor("1506449");
		Disciplina disciplina = ficha.getHabilitacoes().get(0);
		servico.adicionarDisciplina(disciplina.getCodigo());
		servico.adicionarDisciplina(disciplina.getCodigo());
	}
}