package br.cefetrj.sca.dominio;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/applicationContext.xml" })
@TransactionConfiguration
@Transactional
public class HistoricoEscolarTest {

	@Autowired
	AlunoRepositorio alunoRepo;
	
	@Test
	public void testObterDisciplinasPossiveis() {
		assertNotNull("Repositório não definido.", alunoRepo);
		Aluno aluno = alunoRepo.getByMatricula("1311030BCC");
		assertNotNull("Aluno não encontrado.", aluno);
		HistoricoEscolar historico = aluno.getHistorico();
		List<Disciplina> disciplinas = historico.getDisciplinasPossiveis();
		for (Disciplina disciplina : disciplinas) {
			System.out.println(disciplina.getNome());
		}
	}
}
