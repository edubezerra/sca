package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.repositories.CursoRepositorio;
import br.cefetrj.sca.dominio.repositories.VersaoCursoRepositorio;

@Deprecated
public class ImportadorCursos {

	@Autowired
	private CursoRepositorio cursoRepositorio;

	@Autowired
	private VersaoCursoRepositorio versaoCursoRepositorio;

	static String colunas[] = { "ID_DISCIPLINA", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "CH_TEORICA", "CH_PRATICA", "CH_TOTAL",
			"CREDITOS", "ENCARGO_DIDATICO", "IND_HORARIO", "SITUACAO",
			"COD_ESTRUTURADO", "NOME_UNIDADE", "SIGLA_UNIDADE", "COD_CURSO",
			"NUM_VERSAO", "ID_VERSAO_CURSO", "IND_SIM_NAO" };

	/**
	 * Dicionário de pares (sigla, objeto da classe VersaoCurso) de cada curso.
	 */
	private HashMap<String, VersaoCurso> versoesCursos = new HashMap<>();

	/**
	 * Dicionário de pares (sigla, objeto da classe VersaoCurso) de cada curso.
	 */
	private HashMap<String, Curso> cursos = new HashMap<>();

	public ImportadorCursos() {
	}

	public void run() {
		System.out.println("ImportadorCursos.run()");
		try {
			String planilhaCSTSI = "./planilhas/grades-curriculares/DisciplinasCSTSI.xls";
			String planilhaBCC = "./planilhas/grades-curriculares/DisciplinasBCC.xls";

			importarPlanilha(planilhaCSTSI);

			cursos.clear();
			versoesCursos.clear();

			importarPlanilha(planilhaBCC);

		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void gravarCursosMaisVersoes() {

		/**
		 * Realiza a persistência dos objetos Curso.
		 */
		Set<String> cursosIt = cursos.keySet();
		for (String siglaCurso : cursosIt) {
			System.out.println("Gravando " + cursos.get(siglaCurso));
			cursoRepositorio.save(cursos.get(siglaCurso));
		}

		/**
		 * Realiza a persistência dos objetos <code>VersaoCurso</code>.
		 */
		Set<String> versoesIt = versoesCursos.keySet();
		for (String numeroMaisSigla : versoesIt) {
			System.out
					.println("Gravando " + versoesCursos.get(numeroMaisSigla));
			versaoCursoRepositorio.save(versoesCursos.get(numeroMaisSigla));
		}

		System.out.println("Foram importados " + cursos.keySet().size()
				+ " cursos.");
		System.out.println("Foram importados " + versoesCursos.keySet().size()
				+ " versões de cursos.");
	}

	public void importarPlanilha(String inputFile) throws BiffException,
			IOException {
		File inputWorkbook = new File(inputFile);
		System.out.println("Iniciando importação de cursos e suas versões...");
		importarCursosMaisVersoes(inputWorkbook);
		gravarCursosMaisVersoes();
		System.out.println("Cursos e versões importados com sucesso!");
	}

	private void importarCursosMaisVersoes(File inputWorkbook)
			throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {
			String siglaCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"),
					i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"),
					i).getContents();
			String nomeCurso = sheet.getCell(
					colunasList.indexOf("NOME_UNIDADE"), i).getContents();

			if (cursos.get(siglaCurso) == null) {
				Curso curso = new Curso(siglaCurso, nomeCurso);
				cursos.put(siglaCurso, curso);
			}

			if (versoesCursos.get(siglaCurso + numVersao) == null) {
				Curso curso = cursos.get(siglaCurso);
				VersaoCurso versao = new VersaoCurso(numVersao, curso);
				versoesCursos.put(siglaCurso + numVersao, versao);
			}
		}
	}
}
