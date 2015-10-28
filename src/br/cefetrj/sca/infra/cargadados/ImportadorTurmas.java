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

import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.Turma;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 * Realiza a carga de dados de objetos Turma (associações para professores e
 * aulas).
 *
 */

public class ImportadorTurmas {
	/**
	 * Dicionário de pares (matrícula, nome) de cada aluno.
	 */
	private HashMap<String, String> profs_nomes = new HashMap<>();

	/**
	 * Dicionário de pares (código da turma, código da disciplina).
	 */
	private HashMap<String, String> turmas_docentes = new HashMap<>();

	public static void main(String[] args) {
		ImportadorTurmas.run();
	}

	public static void run() {
		System.out.println("ImportadorInformacoesMatricula.main()");
		try {
			String arquivoPlanilha = "./planilhas/ALOCACAO.DOCENTES.2015.1.xls";
			ImportadorTurmas iim = new ImportadorTurmas();
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

		Set<String> ofertasIt = turmas_docentes.keySet();
		for (String codTurma : ofertasIt) {
			Query query;
			Turma turma = null;

			try {
				query = em.createQuery("from Turma t where t.codigo = ?");
				query.setParameter(1, codTurma);
				turma = (Turma) query.getSingleResult();
			} catch (NoResultException e) {
				System.out.println("Turma nao encontrada: " + codTurma);
				turma = null;
			}
			if (turma != null) {
				query = em
						.createQuery("from Professor p where p.matricula = ?");
				query.setParameter(1, turmas_docentes.get(codTurma));

				Professor professor = null;
				try {
					professor = (Professor) query.getSingleResult();
				} catch (NoResultException e) {
					System.out.println("Professor nao encontrado: "
							+ turmas_docentes.get(codTurma));
				}
				if (professor != null) {
					turma.setProfessor(professor);
					em.merge(turma);
				}
			}
		}
		em.getTransaction().commit();
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
				.println("Iniciando importação de dados relativos alocações de docentes a turmas...");

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

			/**
			 * Dados sobre alocações de turmas a professores.
			 */
			String matr_prof = sheet.getCell(
					colunasList.indexOf("MATR_DOCENTE"), i).getContents();

			String turma_codigo = sheet.getCell(
					colunasList.indexOf("COD_TURMA"), i).getContents();

			turmas_docentes.put(turma_codigo, matr_prof);
		}
	}
}
