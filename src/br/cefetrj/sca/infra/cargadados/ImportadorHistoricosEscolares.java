package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.EnumSituacaoFinalAvaliacao;
import br.cefetrj.sca.dominio.HistoricoEscolar;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;

/**
 * @author Eduardo Bezerra
 *
 */
public class ImportadorHistoricosEscolares {

	public ImportadorHistoricosEscolares(String[] codigosCursos) {
	}

	public static void run(EntityManager em, String arquivoPlanilha) {
		System.out.println("ImportadorHistoricosEscolares.main()");
		try {
			String codigosCursos[] = { "BCC" };
			ImportadorHistoricosEscolares iim = new ImportadorHistoricosEscolares(
					codigosCursos);
			iim.importarPlanilha(arquivoPlanilha);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	static String colunas[] = { "MATR_ALUNO", "NOME_PESSOA", "FORMA_EVASAO",
			"COD_TURMA", "COD_DISCIPLINA", "NOME_DISCIPLINA", "ANO", "PERIODO",
			"SITUACAO", "CH_TOTAL", "CREDITOS", "MEDIA_FINAL", "NUM_FALTAS" };

	public void importarPlanilha(String inputFile) throws BiffException,
			IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de históricos escolares...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		for (int i = 1; i < sheet.getRows(); i++) {

			// String codigoCurso = sheet.getCell(
			// colunasList.indexOf("COD_CURSO"), i).getContents();
			//
			// if (!codigosCursos.contains(codigoCurso)) {
			// continue;
			// }

			String aluno_matricula = sheet.getCell(
					colunasList.indexOf("MATR_ALUNO"), i).getContents();

			String disciplina_codigo = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i)
					.getContents();

			String semestre_periodo = sheet.getCell(
					colunasList.indexOf("PERIODO"), i).getContents();

			int ano = Integer.parseInt(semestre_ano);
			SemestreLetivo.EnumPeriodo periodo;

			if (semestre_periodo.equals("1º Semestre")) {
				periodo = EnumPeriodo.PRIMEIRO;
			} else {
				periodo = EnumPeriodo.SEGUNDO;
			}

			SemestreLetivo semestre = new SemestreLetivo(ano, periodo);

			String situacao = sheet.getCell(colunasList.indexOf("SITUACAO"), i)
					.getContents();
			EnumSituacaoFinalAvaliacao situacaoFinal = null;
			if (situacao.equals("Aprovado")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.APROVADO;
			} else if (situacao.equals("Reprovado")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.REPROVADO_POR_MEDIA;
			} else if (situacao.equals("Reprovado por Frequência")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.REPROVADO_POR_FALTAS;
			} else if (situacao.equals("Trancamento de Disciplinas")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.TRANCAMENTO_DISCIPLINA;
			} else if (situacao.equals("Matrícula")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.INDEFINIDA;
			} else if (situacao.equals("Isento por Transferência")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.ISENTO_POR_TRANSFERENCIA;
			} else {
				System.err
						.println("ERRO GRAVE: Valor inválido para a situação final de avaliação!");
				System.exit(1);
			}

			Query queryDisciplina;
			queryDisciplina = em
					.createQuery("from Disciplina a where a.codigo = :codigoDisciplina and "
							+ "a.versaoCurso.numero = :numeroVersao and "
							+ "a.versaoCurso.curso.sigla = :siglaCurso");
			queryDisciplina.setParameter("codigoDisciplina", disciplina_codigo);

			// TODO: generalizar para outros cursos.
			queryDisciplina.setParameter("numeroVersao", "2012");
			queryDisciplina.setParameter("siglaCurso", "BCC");

			Disciplina disciplina = (Disciplina) queryDisciplina
					.getSingleResult();

			Query queryAluno;
			queryAluno = em
					.createQuery("from Aluno a where a.matricula = :matricula");
			queryAluno.setParameter("matricula", aluno_matricula);
			Aluno aluno = (Aluno) queryAluno.getSingleResult();

			HistoricoEscolar he = aluno.getHistorico();
			he.lancar(disciplina, situacaoFinal, semestre);

			em.merge(he);

		}
		em.getTransaction().commit();

		System.out.println("Importação finalizada.");
	}

}
