package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.repositories.CursoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.VersaoCursoRepositorio;

/**
 * Importa objetos <code>Curso</code> <code>VersaoCurso</code> e
 * <code>Disciplina</code>.
 * 
 * @author Eduardo Bezerra
 *
 */
@Component
public class ImportadorGradesCurriculares {

	@Autowired
	DisciplinaRepositorio disciplinaRepositorio;

	@Autowired
	CursoRepositorio cursoRepositorio;

	@Autowired
	VersaoCursoRepositorio versaoCursoRepositorio;

	static String colunasTemp[] = { "ID_DISCIPLINA", "COD_DISCIPLINA", "NOME_DISCIPLINA", "CH_TEORICA", "CH_PRATICA",
			"CH_TOTAL", "CREDITOS", "ENCARGO_DIDATICO", "IND_HORARIO", "SITUACAO", "COD_ESTRUTURADO", "NOME_UNIDADE",
			"SIGLA_UNIDADE", "COD_CURSO", "NUM_VERSAO", "ID_VERSAO_CURSO", "IND_SIM_NAO" };

	static String colunas[] = { "COD_CURSO", "NOME_UNIDADE", "COD_ESTRUTURADO", "COD_DISCIPLINA", "NOME_DISCIPLINA",
			"PERIODO_IDEAL", "CREDITOS", "CH", "CH_TOTAL", "TIPO_AULA_ITEM", "TIPO_AULA", "TIPO_DISCIPLINA",
			"NUM_VERSAO", "SIT_VERSAO", "ID_ATIV_CURRIC", "TOTAL_CH", "TOTAL_CR", "SEMESTRE_IDEAL", "NUM_MIN_PERIODOS",
			"NUM_MAX_PERIODOS", "CH_TOTAL_CURSO", "SIT_ATIV" };

	private HashMap<String, Curso> cursos = new HashMap<>();
	private HashMap<String, VersaoCurso> versoesCursos = new HashMap<>();
	private List<Disciplina> disciplinas = new ArrayList<Disciplina>();

	public void run() {
		try {
			String arquivoPlanilha = "./planilhas/grades-curriculares/11.02.01.99.05-GRADs-MAR.xls";
			this.importarPlanilha(arquivoPlanilha);

			cursos.clear();
			versoesCursos.clear();
			disciplinas.clear();

			arquivoPlanilha = "./planilhas/grades-curriculares/DisciplinasCSTSI.xls";
			this.importarPlanilha(arquivoPlanilha);

		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		String mensagens = importarPlanilha(inputWorkbook);
		System.out.println(mensagens);
	}

	/**
	 * Esse método é o ponto central da funcionalidade fornecida por esta classe.
	 * 
	 * @param arquivoPlanilha
	 * @return
	 * @throws BiffException
	 * @throws IOException
	 */
	public String importarPlanilha(File arquivoPlanilha) throws BiffException, IOException {
		StringBuilder response = new StringBuilder();

		Workbook w;

		response.append("Importação de grade curricular...;");

		List<String> colunasList = Arrays.asList(colunas);

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(arquivoPlanilha, ws);
		Sheet sheet = w.getSheet(0);

		this.verificarExistenciaColunas(colunasList, sheet);

		this.importarCursosComSuasVersoes(colunasList, sheet);
		this.importarDisciplinas(colunasList, sheet);

		response = this.gravarDadosImportados(response);

		return response.toString();
	}

	/**
	 * A primeira linha da planilha deve conter os nomes de cada coluna de
	 * dados. Esse método verifica se a planilha fornecida contém realmente as
	 * colunas na ordem e na quantidade esperadas. Uma exceção é disparada
	 * quando esse não for o caso.
	 * 
	 * @param colunasList
	 * @param sheet
	 */
	private void verificarExistenciaColunas(List<String> colunasList, Sheet sheet) {
		for (String nomeColuna : colunasList) {
			if (!sheet.getCell(colunasList.indexOf(nomeColuna), 0).getContents().equals(nomeColuna)) {
				throw new IllegalArgumentException("Lista de colunas na planilha não é compatível com o esperado.");
			}
		}
	}

	private void importarCursosComSuasVersoes(List<String> colunasList, Sheet sheet) {
		for (int i = 1; i < sheet.getRows(); i++) {
			String codCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			String nomeCurso = sheet.getCell(colunasList.indexOf("NOME_UNIDADE"), i).getContents();
			String situacaoVersaoCurso = sheet.getCell(colunasList.indexOf("SIT_VERSAO"), i).getContents();

			Curso curso = cursos.get(codCurso);
			if (curso == null) {

				curso = cursoRepositorio.findCursoBySigla(codCurso);
				if (curso == null) {
					curso = new Curso(codCurso, nomeCurso);
				}

				cursos.put(codCurso, curso);
			}

			if (versoesCursos.get(codCurso + numVersao) == null) {
				VersaoCurso versao = versaoCursoRepositorio.findByNumeroEmCurso(numVersao, codCurso);
				if (versao == null) {
					versao = new VersaoCurso(numVersao, curso, situacaoVersaoCurso);
				}
				versoesCursos.put(codCurso + numVersao, versao);
			}
		}
	}

	private void importarDisciplinas(List<String> colunasList, Sheet sheet) {
		for (int i = 1; i < sheet.getRows(); i++) {
			String codigoDisciplina = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
			String nomeDisciplina = sheet.getCell(colunasList.indexOf("NOME_DISCIPLINA"), i).getContents();
			String creditos = sheet.getCell(colunasList.indexOf("CREDITOS"), i).getContents();
			String numHoras = sheet.getCell(colunasList.indexOf("CH_TOTAL"), i).getContents();

			String codCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();

			boolean disciplinaJaExiste = false;
			for (Disciplina disciplina : disciplinas) {
				if (disciplina.equals(codigoDisciplina)) {
					disciplinaJaExiste = true;
					break;
				}
			}
			if (!disciplinaJaExiste) {
				Disciplina disciplina = new Disciplina(codigoDisciplina, nomeDisciplina, creditos, numHoras);
				VersaoCurso versaoCurso = versoesCursos.get(codCurso + numVersao);
				disciplina.alocarEmVersao(versaoCurso);
				disciplinas.add(disciplina);
			} else {
				System.err.println("Já existe disciplina com código " + codigoDisciplina);
			}
		}
	}

	private StringBuilder gravarDadosImportados(StringBuilder response) {

		int qtdCursos = 0;
		int qtdDisciplinas = 0;
		int qtdVersoesCursos = 0;

		/**
		 * Realiza a persistência dos objetos <code>Curso</code>.
		 */
		Set<String> cursosIt = cursos.keySet();
		for (String codCurso : cursosIt) {

			Curso curso = cursos.get(codCurso);

			Curso cursoProcurado = cursoRepositorio.findCursoBySigla(codCurso);
			if (cursoProcurado == null) {
				System.out.println("Gravando curso " + codCurso);
				cursoRepositorio.save(curso);
				qtdCursos++;
			} else {
				System.out.println("Curso já existe: " + cursoProcurado);
			}
		}

		/**
		 * Realiza a persistência dos objetos <code>VersaoCurso</code>.
		 */
		Set<String> versoesIt = versoesCursos.keySet();
		for (String numeroMaisSigla : versoesIt) {
			VersaoCurso vc = versoesCursos.get(numeroMaisSigla);
			VersaoCurso vcProcurado = versaoCursoRepositorio.findByNumeroEmCurso(vc.getNumero(),
					vc.getCurso().getSigla());
			if (vcProcurado == null) {
				System.out.println("Gravando " + versoesCursos.get(numeroMaisSigla));
				versaoCursoRepositorio.save(vc);
				qtdVersoesCursos++;
			} else {
				System.out.println("Versão de curso já existe: " + vc);
			}
		}

		/**
		 * Realiza a persistência dos objetos <code>Disciplina</code>.
		 */
		for (Disciplina disciplina : disciplinas) {
			if (disciplinaRepositorio.findByCodigoEmVersaoCurso(disciplina.getCodigo(),
					disciplina.getVersaoCurso()) == null) {
				disciplinaRepositorio.save(disciplina);
				qtdDisciplinas++;
			}
		}

		response.append("Resumo da importação:\n;");
		response.append(qtdCursos + " cursos importados.;");
		response.append(qtdVersoesCursos + " versões de cursos importadas.;");
		response.append(qtdDisciplinas + " disciplinas importadas.;");

		return response;
	}
}
