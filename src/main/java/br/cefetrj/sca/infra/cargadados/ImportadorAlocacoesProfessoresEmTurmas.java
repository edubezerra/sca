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
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.Turma;

/**
 * Esse importador realiza a carga de associações entre objetos
 * <code>Turma</code> e <code>Professor</code>, para um determinado período
 * letivo. Considera que os objtetos objetos <code>Turma</code> e
 * <code>Professor</code> foram importados previamente.
 *
 */
public class ImportadorAlocacoesProfessoresEmTurmas {

	EntityManager em = ImportadorTudo.entityManager;

	String colunas[] = { "COD_DISCIPLINA", "NOME_DISCIPLINA", "COD_TURMA",
			"VAGAS_OFERECIDAS", "DIA_SEMANA", "HR_INICIO", "HR_FIM",
			"TIPO_AULA", "COD_CURSO", "NOME_UNIDADE", "ITEM_TABELA",
			"PERIODO_ITEM", "ANO", "DIA_SEMANA_ITEM", "PERIODO",
			"DT_INICIO_PERIODO", "DT_FIM_PERIODO", "ID_TURMA",
			"NOME_DISCIPLINA_SUB", "MATR_EXTERNA", "NOME_DOCENTE", "ID" };

	/**
	 * Mapeamento de pares (matrícula, nome) de cada aluno.
	 */
	private HashMap<String, String> mapaMatriculasParaNomes = new HashMap<>();

	/**
	 * Mapeamento de pares (código da turma, matrícula do professor responsável).
	 */
	private HashMap<String, String> mapaTurmasParaProfessores = new HashMap<>();

	/**
	 * Mapeamento turmas --> períodos letivos.
	 * 
	 * chave: codigo da turma + código da disciplina,
	 * 
	 * valor: período letivo { ano, período }
	 */
	private HashMap<String, PeriodoLetivo> mapaTurmasParaPeriodos = new HashMap<>();

	public void run() {
		System.out.println("ImportadorAlocacoesProfessoresEmTurmas.run()");
		try {
			String arquivoPlanilha = "./planilhas/turmas-ofertadas/11.02.03.99.05 - Oferta de Disciplinas - Docentes x Cursos - 2015.2.xls";
			ImportadorAlocacoesProfessoresEmTurmas iim = new ImportadorAlocacoesProfessoresEmTurmas();
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	private void gravarDadosImportados() {

		int qtdAlocacoes = 0;
		Set<String> turmasIterator = mapaTurmasParaProfessores.keySet();
		for (String chave : turmasIterator) {

			String[] componentes = chave.split(";");
			String codTurma = componentes[0];
			String codDisciplina = componentes[1];
			
			Query query;
			Turma turma = null;

			PeriodoLetivo periodoLetivo = mapaTurmasParaPeriodos.get(chave);

			try {
				query = em
						.createQuery("from Turma t where t.codigo = ? and t.disciplina.codigo = ? "
								+ "and t.periodo.ano = ? and t.periodo.periodo = ?");
				query.setParameter(1, codTurma);
				query.setParameter(2, codDisciplina);
				query.setParameter(3, periodoLetivo.getAno());
				query.setParameter(4, periodoLetivo.getPeriodo());
				turma = (Turma) query.getSingleResult();
			} catch (NoResultException e) {
				System.err.println("Turma não encontrada: (" + codTurma + ", "
						+ codDisciplina + ")");
				turma = null;
			}
			if (turma != null) {
				query = em
						.createQuery("from Professor p where p.matricula = ?");
				query.setParameter(1, mapaTurmasParaProfessores.get(chave));

				Professor professor = null;
				try {
					professor = (Professor) query.getSingleResult();
				} catch (NoResultException e) {
					System.err.println("Professor não encontrado: "
							+ mapaTurmasParaProfessores.get(chave));
				}
				if (professor != null) {
					turma.setProfessor(professor);
					em.merge(turma);
					qtdAlocacoes++;
				}
			}
		}

		System.out.println("Foram importadas " + qtdAlocacoes
				+ " alocações de professores a turmas.");
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
		System.out
				.println("Iniciando importação de dados relativos alocações de professores a turmas...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			/**
			 * Dados relativos aos docentes.
			 */
			String matriculaProfessor = sheet.getCell(
					colunasList.indexOf("MATR_EXTERNA"), i).getContents();
			String nomeProfessor = sheet.getCell(
					colunasList.indexOf("NOME_DOCENTE"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i)
					.getContents();

			String semestre_periodo = sheet.getCell(
					colunasList.indexOf("PERIODO"), i).getContents();

			PeriodoLetivo semestre = UtilsImportacao.getPeriodoLetivo(
					semestre_ano, semestre_periodo);

			mapaMatriculasParaNomes.put(matriculaProfessor, nomeProfessor);

			/**
			 * Colhe dados sobre alocações de turmas a professores e a períodos
			 * letivos.
			 */

			String codigoTurma = sheet.getCell(
					colunasList.indexOf("COD_TURMA"), i).getContents();

			String codigoDisciplina = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String chave = codigoTurma + ";" + codigoDisciplina;

			mapaTurmasParaProfessores.put(chave, matriculaProfessor);

			mapaTurmasParaPeriodos.put(chave, semestre);

		}
	}
}
