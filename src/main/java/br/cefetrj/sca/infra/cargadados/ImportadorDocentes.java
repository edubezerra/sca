package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import br.cefetrj.sca.dominio.Professor;

/**
 * Este importador realiza a carga de objetos <code>Professor</code>.
 *
 */

public class ImportadorDocentes {

	EntityManager em = ImportadorTudo.entityManager;

	String colunas[] = { "COD_DISCIPLINA", "NOME_DISCIPLINA", "COD_TURMA",
			"VAGAS_OFERECIDAS", "DIA_SEMANA", "HR_INICIO", "HR_FIM",
			"TIPO_AULA", "COD_CURSO", "NOME_UNIDADE", "ITEM_TABELA",
			"PERIODO_ITEM", "ANO", "DIA_SEMANA_ITEM", "PERIODO",
			"DT_INICIO_PERIODO", "DT_FIM_PERIODO", "ID_TURMA",
			"NOME_DISCIPLINA_SUB", "MATR_EXTERNA", "NOME_DOCENTE", "ID" };

	/**
	 * Dicionário de pares <matrícula, nome> de cada professor encontrado na
	 * planilha de entrada.
	 */
	private HashMap<String, String> profs_nomes = new HashMap<>();

	public void run() {
		System.out.println("ImportadorDocentes.main()");
		try {
			String arquivoPlanilha = "./planilhas/turmas-ofertadas/11.02.03.99.05 - Oferta de Disciplinas - Docentes x Cursos - 2015.2.xls";
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

		em.getTransaction().begin();

		Set<String> profsIt = profs_nomes.keySet();
		int adicionados = 0;
		for (String matrProfessor : profsIt) {
			Professor professor;
			try {
				Query queryProfessor;
				queryProfessor = em
						.createQuery("from Professor a where a.matricula = :matricula");
				queryProfessor.setParameter("matricula", matrProfessor);
				professor = (Professor) queryProfessor.getSingleResult();
			} catch (NoResultException e) {
				professor = null;
			}

			if (professor == null) {
				em.persist(new Professor(matrProfessor, profs_nomes
						.get(matrProfessor)));
				adicionados++;
			}
		}
		em.getTransaction().commit();

		System.out.println(">>>Foram importados " + adicionados
				+ " professores.");
	}

	private void importarPlanilha(String arquivoPlanilha) throws BiffException,
			IOException {
		File inputWorkbook = new File(arquivoPlanilha);
		importarPlanilha(inputWorkbook);
	}

	private void importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de docentes...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			/**
			 * Dados relativos aos docentes.
			 */
			String prof_matricula = sheet.getCell(
					colunasList.indexOf("MATR_EXTERNA"), i).getContents();
			String prof_nome = sheet.getCell(
					colunasList.indexOf("NOME_DOCENTE"), i).getContents();

			if (prof_nome.isEmpty()) {
				String nome_disciplina = sheet.getCell(
						colunasList.indexOf("NOME_DISCIPLINA"), i)
						.getContents();
				System.err.println("Turma sem professor para disciplina "
						+ nome_disciplina);
			} else {
				profs_nomes.put(prof_matricula, prof_nome);
			}
		}
	}
}
