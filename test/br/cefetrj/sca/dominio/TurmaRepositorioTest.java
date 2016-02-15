package br.cefetrj.sca.dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.repositorio.DisciplinaRepositorio;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration("file:sca/WebContent")
@ContextConfiguration(locations = { "file:src/applicationContext.xml" })
@Transactional
public class TurmaRepositorioTest {

	@Autowired
	TurmaRepositorio turmaRepositorio;

	@Autowired
	DisciplinaRepositorio disciplinaRepo;

	@Autowired
	AlunoRepositorio alunoRepo;

	@Test
	public void testGravarTurmaNoPeriodoCorrente() {
		assertNotNull("Repositório não definido.", turmaRepositorio);

		Disciplina disciplina = disciplinaRepo.getByNome("MATEMÁTICA DISCRETA",
				"BCC", "2012");
		assertNotNull(disciplina.getId());

		String codigoTurma = "01234";
		Turma turma = new Turma(disciplina, codigoTurma);

		turmaRepositorio.gravar(turma);

		assertNotNull(turma.getId());

		assertEquals(turma.getPeriodo(), PeriodoLetivo.PERIODO_CORRENTE);
	}

	@Test
	public void testGravarTurmaEmPeriodoFornecido() {
		assertNotNull("Repositório não definido.", turmaRepositorio);

		Disciplina disciplina = disciplinaRepo.getByNome("MATEMÁTICA DISCRETA",
				"BCC", "2012");
		assertNotNull(disciplina.getId());

		String codigoTurma = "690011";
		PeriodoLetivo periodo = new PeriodoLetivo(2014, EnumPeriodo.PRIMEIRO);
		Turma turma = new Turma(disciplina, codigoTurma, 40, periodo);

		turmaRepositorio.gravar(turma);

		assertNotNull(turma.getId());

		assertEquals(turma.getPeriodo().getAno().intValue(), 2014);
		assertEquals(turma.getPeriodo().getPeriodo(), EnumPeriodo.PRIMEIRO);
	}

	@Test
	public void testObterTurmaPorCodigoNoPeriodoLetivo() {

		assertNotNull("Repositório não definido.", turmaRepositorio);

		PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
				.getInstance();

		String codigoTurma = "400002";
		PeriodoLetivo periodo = new PeriodoLetivo(2015, EnumPeriodo.SEGUNDO);

		assertEquals(periodo, periodoAvaliacao.getPeriodoLetivo());
		Turma turma = turmaRepositorio.getByCodigoNoPeriodoLetivo(codigoTurma,
				periodo);

		assertNotNull(turma);

		System.out.println(turma.getPeriodo());

		assertEquals(turma.getPeriodo(), periodo);

		assertNotNull(turma.getId());
	}

	@Test
	public void testObterTurmasCursadas() {
		String cpf = "148.323.947-03";
		Aluno aluno = alunoRepo.getAlunoPorCPF(cpf);

		assertEquals("REBECCA PONTES SALLES", aluno.getNome());

		List<Turma> lista = turmaRepositorio.getTurmasCursadas(
				aluno.getMatricula(), new PeriodoLetivo(2015, 2));

		assertEquals(6, lista.size());

		assertEquals(true, listaContemDisciplina(lista, "COMPILADORES"));
		assertEquals(true, listaContemDisciplina(lista, "ALGORITMOS EM GRAFOS"));
		assertEquals(
				true,
				listaContemDisciplina(lista,
						"CONCEPÇÃO E ELABORAÇÃO DE PROJETO FINAL"));
		assertEquals(true,
				listaContemDisciplina(lista, "INFORMÁTICA E SOCIEDADE"));
		assertEquals(true,
				listaContemDisciplina(lista, "LEGISLAÇÃO EM INFORMÁTICA"));
		assertEquals(
				true,
				listaContemDisciplina(lista, "PROJETO E CONSTRUÇÃO DE SISTEMAS"));
	}

	private boolean listaContemDisciplina(List<Turma> lista,
			String nomeDisciplina) {
		for (Turma turma : lista) {
			if (turma.getDisciplina().getNome().equals(nomeDisciplina)) {
				return true;
			}
		}
		return false;
	}

}
