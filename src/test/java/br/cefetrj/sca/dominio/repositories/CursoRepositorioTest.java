package br.cefetrj.sca.dominio.repositories;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Disciplina;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class CursoRepositorioTest {

	@Autowired
	CursoRepositorio cursoRepositorio;

	@Test
	public void testCargaHorariaTotal() {

		assertNotNull("Repositório não definido.", cursoRepositorio);

		String siglaCurso = "BCC";
		String numeroVersao = "2012";

		System.out.println("CH = " + cursoRepositorio.cargaHorariaTotal(siglaCurso, numeroVersao));
	}

	@Test
	public void testFindDisciplinas() {

		assertNotNull("Repositório não definido.", cursoRepositorio);

		String siglaCurso = "BCC";
		String numeroVersao = "2012";

		List<Disciplina> disciplinas = cursoRepositorio.findDisciplinas(siglaCurso, numeroVersao);
		for (Disciplina disciplina : disciplinas) {
			System.out.println(disciplina.getNome() + "\t" + disciplina.getCargaHoraria());
		}
	}
}
