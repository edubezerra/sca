package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

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

	String colunas[] = { "COD_CURSO", "CURSO", "VERSAO_CURSO", "CPF", "MATR_ALUNO", "NOME_PESSOA", "FORMA_EVASAO",
			"COD_TURMA", "COD_DISCIPLINA", "NOME_DISCIPLINA", "ANO", "PERIODO", "SITUACAO", "CH_TOTAL", "CREDITOS",
			"MEDIA_FINAL", "NUM_FALTAS" };

	// "NOME_UNIDADE", "NOME_PESSOA", "CPF", "DT_SOLICITACAO",
	// "DT_PROCESS", "COD_DISCIPLINA", "NOME_DISCIPLINA", "PERIODO_IDEAL",
	// "PRIOR_TURMA", "PRIOR_DISC", "ORDEM_MATR", "SITUACAO", "COD_TURMA",
	// "COD_CURSO", "NUM_VERSAO", "MATR_ALUNO", "SITUACAO_ITEM", "ANO",
	// "PERIODO", "IND_GERADA", "ID_PROCESSAMENTO", "HR_SOLICITACAO" };

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
		String planilhaMatriculas = "./planilhas/historicos-escolares/11.02.05.99.60.xls";
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
		System.out.println("Realizando a persitência de objetos Aluno...");

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência dos objetos Aluno.
		 */
		int adicionados = 0;
		Set<String> matriculas = alunos_matriculas.keySet();
		for (String matricula : matriculas) {
			Aluno aluno;
			try {
				Query queryAluno;
				queryAluno = em.createQuery("from Aluno a where a.matricula = :matricula");
				queryAluno.setParameter("matricula", matricula);
				aluno = (Aluno) queryAluno.getSingleResult();
			} catch (NoResultException e) {
				aluno = null;
			}

			if (aluno == null) {
				em.persist(alunos_matriculas.get(matricula));
				adicionados++;
			}
		}

		em.getTransaction().commit();

		em.close();

		System.out.println("Foram adicionados " + adicionados + " alunos.");
	}

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Realizando leitura da planilha...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String codigoCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numeroVersaoCurso = sheet.getCell(colunasList.indexOf("VERSAO_CURSO"), i).getContents();

			String aluno_matricula = sheet.getCell(colunasList.indexOf("MATR_ALUNO"), i).getContents();
			String aluno_nome = sheet.getCell(colunasList.indexOf("NOME_PESSOA"), i).getContents();
			String aluno_cpf = sheet.getCell(colunasList.indexOf("CPF"), i).getContents();

			if (aluno_cpf == null || aluno_cpf.isEmpty()) {
				System.out.println("CPF não fornecido para aluno " + aluno_nome);
			} else {
				Aluno aluno = alunoFab.criar(aluno_nome, aluno_matricula, aluno_cpf, codigoCurso, numeroVersaoCurso);
				alunos_matriculas.put(aluno_matricula, aluno);
			}

		}
		System.out.println("Dados lidos com sucesso!");
	}
}
