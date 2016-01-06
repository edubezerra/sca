package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;

/**
 * Essa classe faz a carga de objetos <code>Turma</code>.
 * 
 * @author Eduardo Bezerra
 *
 */
public class ImportadorTurmas {

	class Disciplinas {
		private Map<String, Disciplina> disciplinas = new ConcurrentHashMap<String, Disciplina>();

		Disciplina lookup(String siglaCurso, String versaoCurso,
				String codigoDisciplina) {
			String chave = siglaCurso + versaoCurso + codigoDisciplina;
			if (!disciplinas.containsKey(chave)) {

				EntityManagerFactory emf = Persistence
						.createEntityManagerFactory("SCAPU");

				EntityManager em = emf.createEntityManager();

				Query query = em
						.createQuery("from Disciplina d where d.codigo = :codigoDisciplina "
								+ "and d.versaoCurso.numero = :numeroVersaoCurso and d.versaoCurso.curso.sigla = :codCurso");

				query.setParameter("codigoDisciplina", codigoDisciplina);
				query.setParameter("codCurso", siglaCurso);
				query.setParameter("numeroVersaoCurso", versaoCurso);

				Disciplina disciplina;
				try {
					disciplina = (Disciplina) query.getSingleResult();
					disciplinas.put(chave, disciplina);
				} catch (NoResultException e) {
					System.err.println("Disciplina não encontrada. "
							+ "codigoDisciplina: " + codigoDisciplina
							+ ", codCurso: " + siglaCurso
							+ ", numeroVersaoCurso: " + versaoCurso);
					return null;
				}
			}
			return disciplinas.get(chave);
		}
	}

	/**
	 * Dicionário de pares (período letivo + código da turma; código da
	 * disciplina).
	 */
	private HashMap<String, Disciplina> turmas_disciplinas;

	/**
	 * Apenas os cursos cujas siglas (códigos) constam nesta lista são
	 * importados.
	 */
	private static List<String> codigosCursos = new ArrayList<String>();

	static {
		String codigos[] = { "BCC", "WEB" };
		for (String codigo : codigos) {
			codigosCursos.add(codigo);
		}
	}

	public static void main(String[] args) {
		ImportadorTurmas.run();
	}

	public ImportadorTurmas(List<String> codigosCursos) {
		turmas_disciplinas = new HashMap<>();
	}

	public static void run() {
		try {
			String arquivoPlanilha = "./planilhas/matriculas/11.02.05.99.60.xls";
			ImportadorTurmas iim = new ImportadorTurmas(codigosCursos);
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	String colunas[] = { "COD_CURSO", "CURSO", "VERSAO_CURSO", "CPF",
			"MATR_ALUNO", "NOME_PESSOA", "FORMA_EVASAO", "COD_TURMA",
			"COD_DISCIPLINA", "NOME_DISCIPLINA", "ANO", "PERIODO", "SITUACAO",
			"CH_TOTAL", "CREDITOS", "MEDIA_FINAL", "NUM_FALTAS" };

	Disciplinas disciplinas = new Disciplinas();

	public void importarPlanilha(String inputFile) throws BiffException,
			IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out
				.println("Iniciando importação de dados relativos ao seguintes cursos: "
						+ codigosCursos.toString() + " ...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String codigoCurso = sheet.getCell(
					colunasList.indexOf("COD_CURSO"), i).getContents();

			String versaoCurso = sheet.getCell(
					colunasList.indexOf("VERSAO_CURSO"), i).getContents();

			if (!codigosCursos.contains(codigoCurso)) {
				continue;
			}

			String disciplina_codigo = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String turma_codigo = sheet.getCell(
					colunasList.indexOf("COD_TURMA"), i).getContents();

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

			if (turma_codigo.isEmpty()) {
				System.err
						.println("Aviso - código de turma indefinido. Código da disciplina: "
								+ disciplina_codigo);
			} else {
				Disciplina disciplina = disciplinas.lookup(codigoCurso,
						versaoCurso, disciplina_codigo);
				if (disciplina != null) {
					String chave = semestre.toString() + "/" + turma_codigo;
					turmas_disciplinas.put(chave, disciplina);
				}
			}
		}
		System.out.println("Importação finalizada.");
	}

	public void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência de objetos <code>Turma</code>.
		 */
		Set<String> turmasIt = turmas_disciplinas.keySet();

		for (String chave : turmasIt) {
			String[] tokens = chave.split("/");
			Integer ano = Integer.parseInt(tokens[0]);
			Integer num = Integer.parseInt(tokens[1]);
			EnumPeriodo periodo = null;
			if (num.equals(1)) {
				periodo = EnumPeriodo.PRIMEIRO;
			} else if (num.equals(2)) {
				periodo = EnumPeriodo.SEGUNDO;
			} else {
				System.err
						.println("Erro fatal: período letivo não pode ser reconstituído!");
				System.exit(1);
			}
			PeriodoLetivo semestre = new PeriodoLetivo(ano, periodo);
			Disciplina disciplina = turmas_disciplinas.get(chave);

			try {
				int capacidadeMaxima = 80;
				Turma turma = new Turma(disciplina, chave, capacidadeMaxima,
						semestre);
				em.persist(turma);
			} catch (NoResultException e) {
			}
		}

		em.getTransaction().commit();

		em.close();

		System.out.println("Foram importadas "
				+ turmas_disciplinas.keySet().size() + " turmas.");
	}

}
