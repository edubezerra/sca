package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.repositories.VersaoCursoRepositorio;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Component
public class ImportadorPeriodoMinimoVersaoCurso {

	@Autowired
	private VersaoCursoRepositorio versaoCursoRepositorio;
	
	static String colunasPeriodoMinimo[] = {"COD_CURSO", "CURSO", "PERIODO_MINIMO", "NUM_VERSAO"};
	
	public void run() {
		File folder = new File("./planilhas/curso-periodo-minimo");
		File[] listOfFiles = folder.listFiles();

		Scanner in = new Scanner(System.in);
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				int resposta = 1;
				if (resposta == 1) {
					String arquivoPlanilha = "./planilhas/curso-periodo-minimo/" + listOfFiles[i].getName();
					run(arquivoPlanilha);
				}
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Diretório: " + listOfFiles[i].getName());
			}
		}
		in.close();
	}
	
	public void run(String arquivoPlanilha) {
		System.out.println("ImportadorPeriodoMinimoCursos.run()");
		try {
			this.importarPlanilha(arquivoPlanilha);
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
		int importados = 0, versaoCursoNaoEncontrada = 0;
		Workbook w;
		
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);
		
		List<String> colunasList = Arrays.asList(colunasPeriodoMinimo);
		
		for (int i = 1; i < sheet.getRows(); i++) {
			String siglaCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			Integer qtdPeriodoMinimo = Integer.parseInt(sheet.getCell(colunasList.indexOf("PERIODO_MINIMO"), i).getContents());
			String numeroVersao =sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			
			VersaoCurso versaoCurso = versaoCursoRepositorio.findByNumeroEmCurso(numeroVersao, siglaCurso);
			
			if(versaoCurso != null) {
				versaoCurso.setQtdPeriodoMinimo(qtdPeriodoMinimo);
				importados++;
				
				versaoCursoRepositorio.save(versaoCurso);
			} else {
				versaoCursoNaoEncontrada++;
			}
		}
		
		response.append("Foram importados ").append(importados).append(" períodos mínimos de versões de cursos.;");
		response.append("Não foram encontrados ").append(versaoCursoNaoEncontrada).append(" versões de cursos.");
		
		return response.toString();
	}
}
