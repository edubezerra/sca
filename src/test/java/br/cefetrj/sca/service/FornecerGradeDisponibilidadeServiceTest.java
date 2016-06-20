package br.cefetrj.sca.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.gradesdisponibilidade.FichaDisponibilidade;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class FornecerGradeDisponibilidadeServiceTest {

	String matriculaProfessorValida = "1506449";
	String matriculaProfessorInvalida = "121212";

	@Autowired
	protected FornecerGradeDisponibilidadeService servico;

	@Before
	public void init() {
		assertNotNull("Serviço não iniciado.", servico);
	}

	@Test
	public void testValidarProfessorMatriculaInvalida() {
		FichaDisponibilidade ficha = servico
				.validarProfessor(matriculaProfessorInvalida);
		assertNull(ficha);
	}

	@Test
	public void testValidarProfessorMatriculaValida() {
		FichaDisponibilidade ficha = servico
				.validarProfessor(matriculaProfessorValida);
		assertEquals(ficha.getMatriculaProfessor(), matriculaProfessorValida);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testAdicionarMesmaDisciplina() {
		FichaDisponibilidade ficha = servico
				.validarProfessor(matriculaProfessorValida);
		Disciplina disciplina = ficha.getHabilitacoes().get(0);
		servico.adicionarDisciplina(disciplina.getCodigo());
		servico.adicionarDisciplina(disciplina.getCodigo());
	}
}