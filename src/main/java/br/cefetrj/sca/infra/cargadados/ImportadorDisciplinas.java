package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.repositories.CursoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.VersaoCursoRepositorio;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Component
public class ImportadorDisciplinas {

	@Autowired
	DisciplinaRepositorio disciplinaRepositorio;
	
	@Autowired
	CursoRepositorio cursoRepositorio;
	
	@Autowired
	VersaoCursoRepositorio versaoCursoRepositorio;
	
	static String colunas[] = { "ID_DISCIPLINA", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "CH_TEORICA", "CH_PRATICA", "CH_TOTAL",
			"CREDITOS", "ENCARGO_DIDATICO", "IND_HORARIO", "SITUACAO",
			"COD_ESTRUTURADO", "NOME_UNIDADE", "SIGLA_UNIDADE", "COD_CURSO",
			"NUM_VERSAO", "ID_VERSAO_CURSO", "IND_SIM_NAO" };

	private HashMap<String, Curso> cursos = new HashMap<>();
	private HashMap<String, VersaoCurso> versoesCursos = new HashMap<>();
	private List<Disciplina> disciplinas = new ArrayList<Disciplina>();

	public void run() {
		System.out.println("ImportadorGradesCurriculares.run()");
		try {
			String arquivoPlanilha = "./planilhas/grades-curriculares/DisciplinasBCC.xls";
			this.importarPlanilha(arquivoPlanilha);
			this.gravarDadosImportados();

			cursos.clear();
			versoesCursos.clear();
			disciplinas.clear();
			
			arquivoPlanilha = "./planilhas/grades-curriculares/DisciplinasCSTSI.xls";
			this.importarPlanilha(arquivoPlanilha);
			this.gravarDadosImportados();
			
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	private void gravarDadosImportados() {

		/**
		 * Realiza a persistência dos objetos Curso.
		 */
		Set<String> cursosIt = cursos.keySet();
		for (String codCurso : cursosIt) {

			System.out.println("Gravando curso " + codCurso);

			Curso curso = cursos.get(codCurso);

			cursoRepositorio.save(curso);
		}

		/**
		 * Realiza a persistência dos objetos Disciplina.
		 */
		for (Disciplina disciplina : disciplinas) {
			System.out.println("Gravando disciplina: " + disciplina);
			disciplinaRepositorio.save(disciplina);
		}

		System.out.println("Foram importadas " + disciplinas.size()
				+ " disciplinas.");
	}

	private void importarPlanilha(String inputFile) throws BiffException,
			IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	private void importarPlanilha(File arquivoPlanilha) throws BiffException,
			IOException {
		Workbook w;

		System.err.println("Abrindo planilha " + arquivoPlanilha);

		List<String> colunasList = Arrays.asList(colunas);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(arquivoPlanilha, ws);
		Sheet sheet = w.getSheet(0);

		importarVersoesCursos(colunasList, sheet);
		importarDisciplinas(colunasList, sheet);
	}

	private VersaoCurso getVersaoCurso(String siglaCurso, String numeroVersao) {
		return versaoCursoRepositorio.findByNumeroEmCurso(numeroVersao, siglaCurso);
	}

	private void importarVersoesCursos(List<String> colunasList, Sheet sheet) {
		System.out
				.println("Iniciando importação de dados relativos a versões de cursos...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String codCurso = sheet
					.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"),
					i).getContents();
			if (versoesCursos.get(codCurso + numVersao) == null) {
				VersaoCurso versao = getVersaoCurso(codCurso, numVersao);
				versoesCursos.put(codCurso + numVersao, versao);
			}

		}
		System.out.println("Foram encontradas " + versoesCursos.size()
				+ " versões de cursos.");
	}

	private void importarDisciplinas(List<String> colunasList, Sheet sheet) {
		System.out
				.println("Iniciando importação de dados relativos a disciplinas...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String codigoDisciplina = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
			String nomeDisciplina = sheet.getCell(
					colunasList.indexOf("NOME_DISCIPLINA"), i).getContents();
			String creditos = sheet.getCell(colunasList.indexOf("CREDITOS"), i)
					.getContents();
			String numHoras = sheet.getCell(colunasList.indexOf("CH_TOTAL"), i)
					.getContents();

			String codCurso = sheet
					.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"),
					i).getContents();

			boolean disciplinaJaExiste = false;
			for (Disciplina disciplina : disciplinas) {
				if (disciplina.equals(codigoDisciplina)) {
					disciplinaJaExiste = true;
					break;
				}
			}
			if (!disciplinaJaExiste) {
				Disciplina disciplina = new Disciplina(codigoDisciplina,
						nomeDisciplina, creditos, numHoras);
				VersaoCurso versaoCurso = versoesCursos.get(codCurso
						+ numVersao);
				disciplina.alocarEmVersao(versaoCurso);
				disciplinas.add(disciplina);
			} else {
				System.err.println("Já existe disciplina com código "
						+ codigoDisciplina);
			}
		}
	}
}
