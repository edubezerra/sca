package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 * Essa classe faz a carga de objetos <code>Turma</code>.
 * 
 * @author Eduardo Bezerra
 *
 */
public class ImportadorTurmas {

	/**
	 * código, semestre letivo { ano, período }
	 */
	private HashMap<String, SemestreLetivo> turmas;

	/**
	 * Dicionário de pares (código da turma, código da disciplina).
	 */
	private HashMap<String, String> turmas_disciplinas;

	/**
	 * Apenas os cursos cujas siglas (códigos) constam nesta lista são
	 * importados.
	 */
	private static List<String> codigosCursos = new ArrayList<String>();

	static {
		String codigos[] = { "BCC" };
		for (String codigo : codigos) {
			codigosCursos.add(codigo);
		}
	}

	public static void main(String[] args) {
		ImportadorTurmas.run();
	}

	public ImportadorTurmas(List<String> codigosCursos) {
		turmas = new HashMap<>();
		turmas_disciplinas = new HashMap<>();
	}

	public static void run() {
		try {
			String arquivoPlanilha = "./planilhas/MatriculasAceitas-2015.1.xls";
			ImportadorTurmas iim = new ImportadorTurmas(codigosCursos);
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	String colunas[] = { "NOME_UNIDADE", "NOME_PESSOA", "CPF", "DT_SOLICITACAO", "DT_PROCESS", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "PERIODO_IDEAL", "PRIOR_TURMA", "PRIOR_DISC", "ORDEM_MATR", "SITUACAO", "COD_TURMA",
			"COD_CURSO", "MATR_ALUNO", "SITUACAO_ITEM", "ANO", "PERIODO", "IND_GERADA", "ID_PROCESSAMENTO",
			"HR_SOLICITACAO" };

	private static String numeroVersaoCurso = "2012";

	private static String codCurso = "BCC";

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println(
				"Iniciando importação de dados relativos ao seguintes cursos: " + codigosCursos.toString() + " ...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String codigoCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();

			if (!codigosCursos.contains(codigoCurso)) {
				continue;
			}

			String disciplina_codigo = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String turma_codigo = sheet.getCell(colunasList.indexOf("COD_TURMA"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i).getContents();

			String semestre_periodo = sheet.getCell(colunasList.indexOf("PERIODO"), i).getContents();

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
		System.out.println("Importação finalizada.");
	}

	public void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência de objetos <code>Turma</code>.
		 */
		Set<String> turmasIt = turmas.keySet();
		Query query;

		for (String codigoTurma : turmasIt) {
			SemestreLetivo semestre = turmas.get(codigoTurma);
			String codigoDisciplina = turmas_disciplinas.get(codigoTurma);

			query = em.createQuery("from Disciplina d where d.codigo = :codigoDisciplina "
					+ "and d.versaoCurso.numero = :numeroVersaoCurso and d.versaoCurso.curso.sigla = :codCurso");

			query.setParameter("codigoDisciplina", codigoDisciplina);
			query.setParameter("codCurso", codCurso);
			query.setParameter("numeroVersaoCurso", numeroVersaoCurso);

			try {
				Disciplina disciplina = (Disciplina) query.getSingleResult();
				int capacidadeMaxima = 80;
				Turma turma = new Turma(disciplina, codigoTurma, capacidadeMaxima, semestre);
				em.persist(turma);
			} catch (NoResultException e) {
				System.err.println("Disciplina não encontrada. " + "codigoDisciplina: " + codigoDisciplina
						+ ", codCurso: " + codCurso + ", numeroVersaoCurso: " + numeroVersaoCurso);
			}
		}

		em.getTransaction().commit();

		em.close();

		System.out.println("Foram importadas " + turmas.keySet().size() + " turmas.");
	}

}
