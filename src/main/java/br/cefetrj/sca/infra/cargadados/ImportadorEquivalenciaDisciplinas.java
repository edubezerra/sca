package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.BlocoEquivalencia;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.TabelaEquivalencias;
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.repositories.BlocoEquivalenciaRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.TabelaEquivalenciaRepositorio;
import br.cefetrj.sca.dominio.repositories.VersaoCursoRepositorio;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Component
public class ImportadorEquivalenciaDisciplinas {
	
	@Autowired
	private VersaoCursoRepositorio versaoCursoRepositorio;
	
	@Autowired
	private TabelaEquivalenciaRepositorio tabelaEquivalenciaRepositorio;
	
	@Autowired
	private BlocoEquivalenciaRepositorio blocoEquivalenciaRepositorio;
	
	@Autowired
	private DisciplinaRepositorio disciplinaRepositorio;
	
	static String colunas[] = { "COD_CURSO", "NOME_CURSO", "NUM_VERSAO", "DESCR_ESTRUTURA", "COD_DISCIPLINA", "NOME_DISCIPLINA",
			"COD_DISC_EQUIV", "NOME_DISC_EQUIV", "COD_CURSO_DISC_EQUIV", "NOME_CURSO_DISC_EQUIV", "VERSAO_CURSO_DISC_EQUIV", "TIPO_EQUIVALENCIA",
			"BLOCO"};
	
	public void run() {
		System.out.println("ImportadorEquivalenciaDisciplinas.run()");
		File folder = new File("./planilhas/equivalencia-disciplinas");
		File[] listOfFiles = folder.listFiles();

		Scanner in = new Scanner(System.in);
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				String arquivoPlanilha = "./planilhas/equivalencia-disciplinas/" + listOfFiles[i].getName();
				run(arquivoPlanilha);
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Diretório: " + listOfFiles[i].getName());
			}
		}
		in.close();
	}
	
	public void run(String planilha) {
		System.out.println("ImportadorEquivalenciaDisciplinas.run()");
		try {
			importarPlanilha(planilha);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}
	
	private void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		String resultado = importarPlanilha(inputWorkbook);
		System.out.println(resultado);
	}
	
	public String importarPlanilha(File inputWorkbook) throws BiffException, IOException {
		StringBuilder response = new StringBuilder();
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de equivalencia de disciplinas...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {
			String cod_curso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String versao_curso = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			String cod_disciplina = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
			
			String cod_curso_equiv = sheet.getCell(colunasList.indexOf("COD_CURSO_DISC_EQUIV"), i).getContents();
			String versao_curso_equiv = sheet.getCell(colunasList.indexOf("VERSAO_CURSO_DISC_EQUIV"), i).getContents();
			String cod_disciplina_equiv = sheet.getCell(colunasList.indexOf("COD_DISC_EQUIV"), i).getContents();

			// Versao curso e disciplina
			VersaoCurso versaoCursoOriginal = versaoCursoRepositorio.findByNumeroEmCurso(versao_curso, cod_curso);
			
			if(versaoCursoOriginal == null) {
				System.err.println("Versao Curso " + versao_curso + " / " + cod_curso + " inexistente.");
				continue;
			}
			
			Disciplina disciplinaOriginal = disciplinaRepositorio.findByCodigoEmVersaoCurso(cod_disciplina, versaoCursoOriginal);
			
			if(disciplinaOriginal == null) {
				System.err.println("Disciplina codigo: " + cod_disciplina + " para VersaoCurso " + versaoCursoOriginal + " inexistente.");
				continue;
			}
			
			// Versao curso e disciplina equivalente
			VersaoCurso versaoCursoEquivalente = versaoCursoRepositorio.findByNumeroEmCurso(versao_curso_equiv, cod_curso_equiv);
			
			if(versaoCursoEquivalente == null) {
				System.err.println("Versao Curso " + versao_curso_equiv + " / " + cod_curso_equiv + " inexistente.");
				continue;
			}
			
			Disciplina disciplinaEquiv = disciplinaRepositorio.findByCodigoEmVersaoCurso(cod_disciplina_equiv, versaoCursoEquivalente);

			if(disciplinaEquiv == null) {
				System.err.println("Disciplina codigo: " + cod_disciplina_equiv + " para VersaoCurso " + versaoCursoEquivalente + " inexistente.");
				continue;
			}
			
			Set<Disciplina> disciplinasOriginais = new HashSet<>();
			disciplinasOriginais.add(disciplinaOriginal);
			
			Set<Disciplina> disciplinasEquivalentes = new HashSet<>();
			disciplinasEquivalentes.add(disciplinaEquiv);
			
			BlocoEquivalencia bloco = new BlocoEquivalencia(disciplinasOriginais, disciplinasEquivalentes);
			TabelaEquivalencias tabela = new TabelaEquivalencias(versaoCursoOriginal);
			tabela.adicionarBlocoEquivalencia(bloco);
			versaoCursoOriginal.adicionarTabelaEquivalencia(tabela);
			
			blocoEquivalenciaRepositorio.save(bloco);
			tabelaEquivalenciaRepositorio.save(tabela);
			versaoCursoRepositorio.save(versaoCursoOriginal);
		}

		response.append("Importação de equivalência de disciplinas finalizada.;");
		response.append("Quantidade total de registros da planilha: " + (sheet.getRows() - 1) + ";");

		return response.toString();
	}
}
