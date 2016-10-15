package br.cefetrj.sca.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Turma;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class RegistrarFrequenciaServiceTest {

	@Autowired
	RegistrarFrequenciaService registrarFrequenciaService;
	
	@Test
	public void iniciarRegistroFreqTurmaTest(){
		List<Turma> turmasProfessor =  registrarFrequenciaService.iniciarRegistroFreqTurma("1506449");
		assertNotNull(turmasProfessor);
		assertTrue(!turmasProfessor.isEmpty());
	}

	
}
