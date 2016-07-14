package br.cefetrj.sca.dominio;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.config.AppConfig;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@Transactional
public class HistoricoEscolarTest {

	@Autowired
	AlunoRepositorio alunoRepo;

	String[] nomesDisciplinas = { "CÁLCULO A UMA VARIÁVEL", "ÁLGEBRA LINEAR I",
			"MICROECONOMIA", "CÁLCULO VETORIAL",
			"EQUAÇÕES DIFERENCIAIS PARCIAIS E SÉRIES", "ECONOMIA BRASILEIRA",
			"GESTÃO ESTRATÉGICA", "SIMULAÇÕES EMPRESARIAIS",
			"RESPONSABILIDADE SOCIAL", "VARIÁVEIS COMPLEXAS",
			"EXPRESSÃO ORAL E ESCRITA", "INTRODUÇÃO À ADMINISTRAÇÃO",
			"ARQUITETURA DE COMPUTADORES",
			"PROJETO DE ALGORITMOS COMPUTACIONAIS", "LÓGICA MATEMÁTICA",
			"MATEMÁTICA DISCRETA", "SISTEMAS DIGITAIS",
			"FUNDAMENTOS DE REDES DE COMPUTADORES", "ENGENHARIA DE REQUISITOS",
			"EMPREENDEDORISMO", "METODOLOGIA CIENTÍFICA",
			"TEORIA DA COMPUTAÇÃO", "LEGISLAÇÃO EM INFORMÁTICA",
			"ESTÁGIO SUPERVISIONADO", "PROGRAMAÇÃO DE CLIENTES WEB",
			"CIÊNCIAS AMBIENTAIS", "HUMANIDADES E CIÊNCIAS SOCIAIS",
			"SEGURANÇA DA INFORMAÇÃO", "INFORMÁTICA E SOCIEDADE" };

	@Test
	public void testObterDisciplinasPossiveis() {
//		assertNotNull("Repositório não definido.", alunoRepo);
//		Aluno aluno = alunoRepo.findAlunoByMatricula("1311030BCC");
//		assertNotNull("Aluno não encontrado.", aluno);
//		HistoricoEscolar historico = aluno.getHistorico();
//		List<Disciplina> disciplinas = historico.getDisciplinasPossiveis();
//
//		List<String> nomes = Arrays.asList(nomesDisciplinas);
//
//		for (Disciplina disciplina : disciplinas) {
//			System.out.println(disciplina.getNome());
//			assertTrue(nomes.contains(disciplina.getNome()));
//		}
	}
}
