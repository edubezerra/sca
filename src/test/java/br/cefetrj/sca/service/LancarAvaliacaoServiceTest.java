package br.cefetrj.sca.service;

import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.FichaAvaliacoes;
import br.cefetrj.sca.dominio.Inscricao;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class LancarAvaliacaoServiceTest {

	String matriculaProfessor = "1506449";
	private Professor professor;
	Turma turma;

	@Autowired
	private LancarAvaliacoesService servico;

	@Autowired
	protected TurmaRepositorio turmaRepositorio;

	@Autowired
	protected ProfessorRepositorio professorRepo;

	@Before
	public void init() {
		assertNotNull("Serviço não iniciado.", servico);
		professor = professorRepo.findProfessorByMatricula(matriculaProfessor);
		turma = turmaRepositorio.findAll().get(0);
	}

	@Test
	public void testIniciarLancamentoNotas() {
		List<Turma> lista = servico.iniciarLancamentoNotas(professor
				.getMatricula());
		assertNotNull(lista);
	}

	@Test
	public void testLancarNotaValida() {
		FichaAvaliacoes ficha = servico.solicitarLancamento(turma.getId());
		Set<Inscricao> inscricoes = ficha.getTurma().getInscricoes();
		Iterator<Inscricao> iter = inscricoes.iterator();
		Aluno a = iter.next().getAluno();
		ficha.lancar(a.getMatricula(), new BigDecimal(10), new BigDecimal(10),
				null, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testLancarNotaInvalida() {
		FichaAvaliacoes ficha = servico.solicitarLancamento(turma.getId());

		Set<Inscricao> inscricoes = ficha.getTurma().getInscricoes();
		Iterator<Inscricao> iter = inscricoes.iterator();
		Aluno a = iter.next().getAluno();
		ficha.lancar(a.getMatricula(), new BigDecimal(-1), new BigDecimal(10),
				null, 0);
	}

	@Test
	public void testConfirmarLancamentoAvaliacoes() {
		FichaAvaliacoes ficha = servico.solicitarLancamento(turma.getId());
		Set<Inscricao> inscricoes = ficha.getTurma().getInscricoes();
		for (Inscricao i : inscricoes) {
			Aluno a = i.getAluno();
			ficha.lancar(a.getMatricula(), new BigDecimal(9.5), new BigDecimal(
					9.0), null, 0);
		}
		servico.encerrarLancamentos(ficha);
	}
}
