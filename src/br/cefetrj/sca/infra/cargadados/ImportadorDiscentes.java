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

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoFabrica;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorDiscentes {
	private ApplicationContext context;

	String colunas[] = { "NOME_UNIDADE", "NOME_PESSOA", "CPF", "DT_SOLICITACAO", "DT_PROCESS", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "PERIODO_IDEAL", "PRIOR_TURMA", "PRIOR_DISC", "ORDEM_MATR", "SITUACAO", "COD_TURMA",
			"COD_CURSO", "MATR_ALUNO", "SITUACAO_ITEM", "ANO", "PERIODO", "IND_GERADA", "ID_PROCESSAMENTO",
			"HR_SOLICITACAO" };

	/**
	 * Dicionário de pares (matrícula, objeto da classe aluno) de cada aluno.
	 */
	private HashMap<String, Aluno> alunos_matriculas = new HashMap<>();

	private AlunoFabrica alunoFab;

	public ImportadorDiscentes() {
		context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
		alunoFab = (AlunoFabrica) context.getBean("AlunoFabricaBean");
	}

	public static void main(String[] args) {
		String planilhaMatriculas = "./planilhas/MatriculasAceitas-2015.1.xls";
		ImportadorDiscentes.run(planilhaMatriculas);
	}

	public static void run(String arquivoPlanilha) {
		System.out.println("ImportadorDiscentes.run()");
		try {
			ImportadorDiscentes iim = new ImportadorDiscentes();
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
		 * Realiza a persistência dos objetos Aluno.
		 */
		Set<String> alunosIt = alunos_matriculas.keySet();
		for (String matricula : alunosIt) {
			em.persist(alunos_matriculas.get(matricula));
		}

		em.getTransaction().commit();

		System.out.println("Foram importados " + alunos_matriculas.keySet().size() + " alunos.");
	}

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de alunos...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String codigoCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();

			String aluno_matricula = sheet.getCell(colunasList.indexOf("MATR_ALUNO"), i).getContents();
			String aluno_nome = sheet.getCell(colunasList.indexOf("NOME_PESSOA"), i).getContents();
			String aluno_cpf = sheet.getCell(colunasList.indexOf("CPF"), i).getContents();

			Aluno aluno = alunoFab.criar(aluno_nome, aluno_matricula, aluno_cpf, codigoCurso);

			alunos_matriculas.put(aluno_matricula, aluno);
		}
	}
}
