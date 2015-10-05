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

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.dominio.Curso;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorCursos {
	private static ApplicationContext context = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	String colunas[] = { "NOME_UNIDADE", "NOME_PESSOA", "CPF", "DT_SOLICITACAO", "DT_PROCESS", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "PERIODO_IDEAL", "PRIOR_TURMA", "PRIOR_DISC", "ORDEM_MATR", "SITUACAO", "COD_TURMA",
			"COD_CURSO", "MATR_ALUNO", "SITUACAO_ITEM", "ANO", "PERIODO", "IND_GERADA", "ID_PROCESSAMENTO",
			"HR_SOLICITACAO" };

	/**
	 * Dicionário de pares (sigla, objeto da classe Curso) de cada curso.
	 */
	private HashMap<String, Curso> cursos_siglas = new HashMap<>();

	public ImportadorCursos() {
	}

	public static void main(String[] args) {
		String planilhaMatriculas = "./planilhas/MatriculasAceitas-2015.1.xls";
		ImportadorCursos.run(planilhaMatriculas);
	}

	public static void run(String arquivoPlanilha) {
		System.out.println("ImportadorCursos.run()");
		try {
			ImportadorCursos iim = new ImportadorCursos();
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	public void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência dos objetos Curso.
		 */
		Set<String> alunosIt = cursos_siglas.keySet();
		for (String siglaCurso : alunosIt) {
			em.persist(cursos_siglas.get(siglaCurso));
		}

		em.getTransaction().commit();

		System.out.println("Foram importados " + cursos_siglas.keySet().size() + " cursos.");
	}

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de cursos...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String siglaCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String nomeCurso = sheet.getCell(colunasList.indexOf("NOME_UNIDADE"), i).getContents();

			if (cursos_siglas.get(siglaCurso) == null) {
				cursos_siglas.put(nomeCurso, new Curso(siglaCurso, nomeCurso));
			}
		}
	}
}
