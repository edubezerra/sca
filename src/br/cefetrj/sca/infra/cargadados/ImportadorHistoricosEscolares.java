package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.EnumSituacaoAvaliacao;
import br.cefetrj.sca.dominio.HistoricoEscolar;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;

/**
 * @author Eduardo Bezerra
 *
 */
public class ImportadorHistoricosEscolares {

	public ImportadorHistoricosEscolares(String[] codigosCursos) {
	}

	public static void main(String[] args) {
		File folder = new File("./planilhas/historicos-escolares");
		File[] listOfFiles = folder.listFiles();

		Scanner in = new Scanner(System.in);
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				// System.out.println("Importar dados da planilha \""
				// + listOfFiles[i].getName()
				// + "\"? Sim = 1; Não: qq outro dígito.");
				// int resposta = in.nextInt();
				int resposta = 1;
				if (resposta == 1) {
					String arquivoPlanilha = "./planilhas/historicos-escolares/"
							+ listOfFiles[i].getName();
					ImportadorHistoricosEscolares.run(arquivoPlanilha);
				}
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Diretório: " + listOfFiles[i].getName());
			}
		}
		in.close();
	}

	public static void run(String planilha) {
		System.out.println("ImportadorHistoricosEscolares.main()");
		try {
			String codigosCursos[] = { "BCC" };
			ImportadorHistoricosEscolares iim = new ImportadorHistoricosEscolares(
					codigosCursos);
			iim.importarPlanilha(planilha);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	static String colunas[] = { "COD_CURSO", "CURSO", "VERSAO_CURSO", "CPF",
			"MATR_ALUNO", "NOME_PESSOA", "FORMA_EVASAO", "COD_TURMA",
			"COD_DISCIPLINA", "NOME_DISCIPLINA", "ANO", "PERIODO", "SITUACAO",
			"CH_TOTAL", "CREDITOS", "MEDIA_FINAL", "NUM_FALTAS" };

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

			String cod_curso = sheet.getCell(colunasList.indexOf("COD_CURSO"),
					i).getContents();

			String versao_curso = sheet.getCell(
					colunasList.indexOf("VERSAO_CURSO"), i).getContents();

			String aluno_matricula = sheet.getCell(
					colunasList.indexOf("MATR_ALUNO"), i).getContents();

			String cod_disciplina = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i)
					.getContents();

			String semestre_periodo = sheet.getCell(
					colunasList.indexOf("PERIODO"), i).getContents();

			int ano = Integer.parseInt(semestre_ano);
			PeriodoLetivo.EnumPeriodo periodo;

			if (semestre_periodo.equals("1º Semestre")) {
				periodo = EnumPeriodo.PRIMEIRO;
			} else {
				periodo = EnumPeriodo.SEGUNDO;
			}

			PeriodoLetivo semestre = new PeriodoLetivo(ano, periodo);

			String situacao = sheet.getCell(colunasList.indexOf("SITUACAO"), i)
					.getContents();

			EnumSituacaoAvaliacao situacaoFinal = null;
			if (situacao.equals("Aprovado")) {
				situacaoFinal = EnumSituacaoAvaliacao.APROVADO;
			} else if (situacao.equals("Reprovado")) {
				situacaoFinal = EnumSituacaoAvaliacao.REPROVADO_POR_MEDIA;
			} else if (situacao.equals("Reprovado por Frequência")) {
				situacaoFinal = EnumSituacaoAvaliacao.REPROVADO_POR_FALTAS;
			} else if (situacao.equals("Reprovado sem nota")) {
				situacaoFinal = EnumSituacaoAvaliacao.REPROVADO_SEM_NOTA;
			} else if (situacao.equals("Trancamento de Disciplinas")) {
				situacaoFinal = EnumSituacaoAvaliacao.TRANCAMENTO_DISCIPLINA;
			} else if (situacao.equals("Matrícula")) {
				situacaoFinal = EnumSituacaoAvaliacao.INDEFINIDA;
			} else if (situacao.equals("Isento por Transferência")) {
				situacaoFinal = EnumSituacaoAvaliacao.ISENTO_POR_TRANSFERENCIA;
			} else if (situacao.equals("Trancamento Total")) {
				situacaoFinal = EnumSituacaoAvaliacao.TRANCAMENTO_TOTAL;
			} else if (situacao.equals("Aproveitamento por Estudos")) {
				situacaoFinal = EnumSituacaoAvaliacao.APROVEITAMENTO_POR_ESTUDOS;
			} else if (situacao.equals("Aproveitamento de Créditos")) {
				situacaoFinal = EnumSituacaoAvaliacao.APROVEITAMENTO_CREDITOS;
			} else if (situacao.equals("Isento")) {
				situacaoFinal = EnumSituacaoAvaliacao.ISENTO;
			} else {
				System.err
						.println("ERRO GRAVE: Valor inválido para a situação final de avaliação! "
								+ situacao);
				System.exit(1);
			}

			if (cod_disciplina.equals("TRT001")) {
				/**
				 * Esse código representa trancamento do período.
				 * 
				 * TODO: implementar o registro dessa situação no histórico
				 * escolar do aluno.
				 */
				continue;
			}

			Query queryDisciplina;
			queryDisciplina = em
					.createQuery("from Disciplina a where a.codigo = :codDisciplina and "
							+ "a.versaoCurso.numero = :numeroVersao and "
							+ "a.versaoCurso.curso.sigla = :siglaCurso");

			queryDisciplina.setParameter("codDisciplina", cod_disciplina);
			queryDisciplina.setParameter("siglaCurso", cod_curso);
			queryDisciplina.setParameter("numeroVersao", versao_curso);

			Disciplina disciplina = null;
			try {
				disciplina = (Disciplina) queryDisciplina.getSingleResult();
			} catch (NoResultException e) {
				System.err.println("Disciplina não encontrada (código): "
						+ cod_disciplina);
			}

			if (disciplina != null) {
				Aluno aluno = null;
				try {
					Query queryAluno;
					queryAluno = em
							.createQuery("from Aluno a where a.matricula = :matricula");
					queryAluno.setParameter("matricula", aluno_matricula);
					aluno = (Aluno) queryAluno.getSingleResult();
				} catch (NoResultException e) {
					System.err.println("Aluno não encontrado. Matrícula: "
							+ aluno_matricula);
				}

				if (aluno != null) {
					aluno.registrarNoHistoricoEscolar(disciplina,
							situacaoFinal, semestre);
					em.merge(aluno);
					System.out.println("Lançada disciplina "
							+ disciplina.toString()
							+ " no histórico escolar do aluno de matrícula "
							+ aluno.getMatricula());
				}

			}
		}

		em.getTransaction().commit();

		em.close();

		System.out.println("Importação de históricos finalizada.");
	}

}
