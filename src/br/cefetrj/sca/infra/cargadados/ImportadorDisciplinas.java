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
import javax.persistence.Persistence;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.VersaoGradeCurso;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorDisciplinas {

	String colunas[] = { "COD_CURSO", "NUM_VERSAO", "DESCR_ESTRUTURA", "COD_DISCIPLINA", "NOME_UNIDADE",
			"COD_ESTRUTURADO", "NOME_DISCIPLINA", "PERIODO_IDEAL", "CREDITOS", "TIPO_AULA", "NUM_HORAS", "CONTA_CH",
			"TIPO_DISCIPLINA", "SITUACAO_VERSAO", "EMENTA", "CH_TOTAL", "ID_CURSO", "ID_VERSAO_CURSO",
			"ID_ESTRUTURA_CUR", "DESCR_SITUACAO" };

	private HashMap<String, VersaoGradeCurso> disciplinas_versoes = new HashMap<>();

	private static HashMap<String, Curso> cursos = new HashMap<>();
	private static HashMap<String, List<VersaoGradeCurso>> versoesCursos = new HashMap<>();

	private static List<Disciplina> disciplinas = new ArrayList<Disciplina>();

	public static void main(String[] args) {
		ImportadorDisciplinas.run();
	}

	public static void run() {
		System.out.println("ImportadorGradesCurriculares.run()");
		try {
			ImportadorDisciplinas iim = new ImportadorDisciplinas();
			String arquivoPlanilha = "./planilhas/grades-curriculares/BCC-grade-2012.2.xls";
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();

		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	private void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência dos objetos Curso.
		 */
		Set<String> cursosIt = cursos.keySet();
		for (String codCurso : cursosIt) {

			System.out.println("Gravando curso " + codCurso);

			Curso curso = cursos.get(codCurso);

			em.persist(curso);
		}

		/**
		 * Realiza a persistência dos objetos Disciplina.
		 */
		for (Disciplina disciplina : disciplinas) {
			VersaoGradeCurso versaoCurso = disciplinas_versoes.get(disciplina.getCodigo());
			disciplina.alocarEmVersao(versaoCurso);
			em.persist(disciplina);
			System.out.println("Gravando disciplina: " + disciplina);
		}

		em.getTransaction().commit();

		System.out.println("Foram importadas " + disciplinas.size() + " disciplinas.");
	}

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	private void importarPlanilha(File arquivoPlanilha) throws BiffException, IOException {
		Workbook w;

		System.err.println("Abrindo planilha " + arquivoPlanilha);

		List<String> colunasList = Arrays.asList(colunas);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(arquivoPlanilha, ws);
		Sheet sheet = w.getSheet(0);

		importarCursos(colunasList, sheet);
		importarVersoesCursos(colunasList, sheet);
		importarDisciplinas(colunasList, sheet);
	}

	private void importarCursos(List<String> colunasList, Sheet sheet) {
		System.out.println("Iniciando importação de dados relativos a cursos...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String siglaCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String nome_unidade = sheet.getCell(colunasList.indexOf("NOME_UNIDADE"), i).getContents();
			cursos.put(siglaCurso, new Curso(siglaCurso, nome_unidade));
		}
		System.out.println("Foram encontrados " + cursos.size() + " cursos.");
	}

	private void importarVersoesCursos(List<String> colunasList, Sheet sheet) {
		System.out.println("Iniciando importação de dados relativos a versões de cursos...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String cod_curso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			List<VersaoGradeCurso> versoes = versoesCursos.get(cod_curso);
			if (versoes == null) {
				versoes = new ArrayList<>();
			}
			if (versoesCursos.put(cod_curso, versoes) == null) {
				versoes.add(new VersaoGradeCurso(numVersao));
			}

		}

		Set<String> cursosIt = cursos.keySet();
		for (String codCurso : cursosIt) {
			Curso curso = cursos.get(codCurso);
			List<VersaoGradeCurso> versoes = versoesCursos.get(codCurso);
			for (VersaoGradeCurso versao : versoes) {
				System.out.println("Registrando versão " + versao.getNumero() + " para curso " + curso.getSigla());
				curso.registrarVersao(versao.getNumero());
			}
		}
		System.out.println("Foram encontradas " + versoesCursos.size() + " versões de cursos.");
	}

	private void importarDisciplinas(List<String> colunasList, Sheet sheet) {
		System.out.println("Iniciando importação de dados relativos a disciplinas...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String codigoDisciplina = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
			String nome_disciplina = sheet.getCell(colunasList.indexOf("NOME_DISCIPLINA"), i).getContents();
			String creditos = sheet.getCell(colunasList.indexOf("CREDITOS"), i).getContents();
			String num_horas = sheet.getCell(colunasList.indexOf("NUM_HORAS"), i).getContents();
			String periodoIdeal = sheet.getCell(colunasList.indexOf("PERIODO_IDEAL"), i).getContents();
			disciplinas.add(new Disciplina(codigoDisciplina, nome_disciplina, creditos, num_horas, periodoIdeal));

			String cod_curso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();

			Curso curso = cursos.get(cod_curso);
			VersaoGradeCurso versao = curso.getVersao(numVersao);
			if (versao == null) {
				System.out.println("Versão " + numVersao + " não encontrada no curso " + cod_curso);
				System.exit(1);
			}
			disciplinas_versoes.put(codigoDisciplina, versao);
			System.out.println("Versão da disciplina " + codigoDisciplina + " eh " + versao);
		}
	}
}
