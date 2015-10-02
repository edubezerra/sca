package br.cefetrj.sca.infra.autoavaliacao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.persistence.EntityManager;

import br.cefetrj.sca.dominio.Disciplina;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorGradesCurriculares {

	String colunas[] = { "COD_CURSO", "NUM_VERSAO", "DESCR_ESTRUTURA", "COD_DISCIPLINA", "NOME_UNIDADE",
			"COD_ESTRUTURADO", "NOME_DISCIPLINA", "PERIODO_IDEAL", "CREDITOS", "TIPO_AULA", "NUM_HORAS", "CONTA_CH",
			"TIPO_DISCIPLINA", "SITUACAO_VERSAO", "EMENTA", "CH_TOTAL", "ID_CURSO", "ID_VERSAO_CURSO",
			"ID_ESTRUTURA_CUR", "DESCR_SITUACAO" };

	private static HashMap<String, String> cursos = new HashMap<String, String>();
	private static HashMap<String, String> versoesCursos = new HashMap<String, String>();

	private static List<Disciplina> disciplinas = new ArrayList<Disciplina>();

	public static void run(EntityManager em, String arquivoPlanilha) {
		System.out.println("ImportadorGradesCurriculares.run()");
		try {
			ImportadorGradesCurriculares iim = new ImportadorGradesCurriculares();
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
			System.out.println("Foram importadas " + disciplinas.size() + " disciplinas.");

		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	private void gravarDadosImportados() {
		// TODO Auto-generated method stub

	}

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	private void importarPlanilha(File arquivoPlanilha) throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(arquivoPlanilha, ws);
		Sheet sheet = w.getSheet(0);

		importarCursos(colunasList, sheet);
		importarVersoesCursos(colunasList, sheet);
		importarComponentesCurriculares(colunasList, sheet);
	}

	private void importarComponentesCurriculares(List<String> colunasList, Sheet sheet) {
		for (int i = 1; i < sheet.getRows(); i++) {
			String codigoDisciplina = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
			String nome_disciplina = sheet.getCell(colunasList.indexOf("NOME_DISCIPLINA"), i).getContents();
			String creditos = sheet.getCell(colunasList.indexOf("CREDITOS"), i).getContents();
			String num_horas = sheet.getCell(colunasList.indexOf("NUM_HORAS"), i).getContents();
			String periodoIdeal = sheet.getCell(colunasList.indexOf("PERIODO_IDEAL"), i).getContents();
			disciplinas.add(new Disciplina(codigoDisciplina, nome_disciplina, creditos, num_horas, periodoIdeal));
		}
	}

	private void importarCursos(List<String> colunasList, Sheet sheet) {
		System.out.println("Iniciando importação de dados relativos a cursos...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String cod_curso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String nome_unidade = sheet.getCell(colunasList.indexOf("NOME_UNIDADE"), i).getContents();
			cursos.put(cod_curso, nome_unidade);
		}
	}

	private void importarVersoesCursos(List<String> colunasList, Sheet sheet) {
		System.out.println("Iniciando importação de dados relativos a cursos...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String cod_curso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String num_unidade = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			versoesCursos.put(cod_curso, num_unidade);
		}
	}
}
