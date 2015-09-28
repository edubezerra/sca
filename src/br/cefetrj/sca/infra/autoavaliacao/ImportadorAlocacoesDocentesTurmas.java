package br.cefetrj.sca.infra.autoavaliacao;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorAlocacoesDocentesTurmas {
	/**
	 * Dicionário de pares (matrícula, nome) de cada aluno.
	 */
	private HashMap<String, String> profs_nomes;

	/**
	 * código, semestre letivo { ano, período }
	 */
	private HashMap<String, SemestreLetivo> turmas;

	/**
	 * Dicionário de pares (código da turma, código da disciplina).
	 */
	private HashMap<String, String> turmas_disciplinas;

	public static void run(EntityManager em, String arquivoPlanilha) {
		System.out.println("ImportadorInformacoesMatricula.main()");
		try {
			ImportadorAlocacoesDocentesTurmas iim = new ImportadorAlocacoesDocentesTurmas();
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	private void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();
		em.getTransaction().commit();

	}

	private void importarPlanilha(String arquivoPlanilha) throws BiffException,
			IOException {
		File inputWorkbook = new File(arquivoPlanilha);
		importarPlanilha(inputWorkbook);
	}

	String colunas[] = { "COD_DISCIPLINA", "NOME_DISCIPLINA", "COD_TURMA",
			"COD_CURSO", "NOME_UNIDADE", "ANO", "PERIODO", "NOME_DOCENTE",
			"MATR_DOCENTE" };

	private void importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out
				.println("Iniciando importação de dados relativos alocações de docentes a turmas...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			
			/**
			 * Importa dados relativos aos docentes.
			 */
			String prof_matricula = sheet.getCell(
					colunasList.indexOf("MATR_DOCENTE"), i).getContents();
			String prof_nome = sheet.getCell(
					colunasList.indexOf("NOME_DOCENTE"), i).getContents();

			profs_nomes.put(prof_matricula, prof_nome);

			/**
			 * Importa dados sobre alocações de turmas a professores.
			 */
			String disciplina_codigo = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String turma_codigo = sheet.getCell(
					colunasList.indexOf("COD_TURMA"), i).getContents();

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
			turmas.put(turma_codigo, semestre);
			turmas_disciplinas.put(turma_codigo, disciplina_codigo);
		}
	}
}
