package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import br.cefetrj.sca.dominio.Professor;

/**
 * Realiza a carga de objetos Professor.
 *
 */

public class ImportadorDocentes {
	/**
	 * Dicionário de pares (matrícula, nome) de cada aluno.
	 */
	private HashMap<String, String> profs_nomes = new HashMap<>();

	public static void main(String[] args) {
		ImportadorDocentes.run();
	}

	public static void run() {
		System.out.println("ImportadorDocentes.main()");
		try {
			String arquivoPlanilha = "./planilhas/ALOCACAO.DOCENTES.2015.1.xls";
			ImportadorDocentes iim = new ImportadorDocentes();
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

		Set<String> profsIt = profs_nomes.keySet();
		for (String matrProfessor : profsIt) {
			em.persist(new Professor(matrProfessor, profs_nomes
					.get(matrProfessor)));
		}
		em.getTransaction().commit();

		System.out.println("Foram importados " + profs_nomes.keySet().size() + " professores.");
	}

	private void importarPlanilha(String arquivoPlanilha) throws BiffException,
			IOException {
		File inputWorkbook = new File(arquivoPlanilha);
		importarPlanilha(inputWorkbook);
	}

	String colunas[] = { "COD_DISCIPLINA", "NOME_DISCIPLINA", "COD_TURMA",
			"TIPO_AULA", "COD_CURSO", "NOME_UNIDADE", "ANO", "PERIODO",
			"NOME_DOCENTE", "MATR_DOCENTE" };

	private void importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out
				.println("Iniciando importação de docentes...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			/**
			 * Dados relativos aos docentes.
			 */
			String prof_matricula = sheet.getCell(
					colunasList.indexOf("MATR_DOCENTE"), i).getContents();
			String prof_nome = sheet.getCell(
					colunasList.indexOf("NOME_DOCENTE"), i).getContents();

			profs_nomes.put(prof_matricula, prof_nome);
		}
	}
}
