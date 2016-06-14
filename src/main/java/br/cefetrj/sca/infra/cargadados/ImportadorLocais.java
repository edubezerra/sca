package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import br.cefetrj.sca.dominio.LocalAula;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorLocais {

	String colunas[] = { "COD_CURSO", "COD_DISCIPLINA", "COD_TURMA",
			"PERIODO_IDEAL", "NOME_DISCIPLINA", "DIA_SEMANA", "HR_INICIO",
			"HR_FIM", "TIPO_AULA", "NOME_UNIDADE", "ANO", "PERIODO", "NUM_SALA" };

	private HashMap<String, LocalAula> locais_desc = new HashMap<>();

	public ImportadorLocais() {

	}

	public void run() {
		
		String planilhaMatriculas = "./planilhas/GRADUACAO.SALAS.2014.2.MARACANA.xls";
		this.run(planilhaMatriculas);
		
	}

	public void run(String arquivoPlanilha) {
		
		System.out.println("ImportadorLocal.run()");
		
		try {
			
			ImportadorLocais impLocal = new ImportadorLocais();
			impLocal.importarPlanilha(arquivoPlanilha);
			impLocal.gravarDadosImportados();
			
		} catch (BiffException | IOException e) {
			
			e.printStackTrace();
			System.exit(1);
			
		}
		System.out.println("Feito!");
	}

	public void gravarDadosImportados() {

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		Set<String> locaisDesc = locais_desc.keySet();
		
		for (String desc : locaisDesc) {
			
			em.persist(locais_desc.get(desc));
			
		}

		em.getTransaction().commit();

		em.close();

		System.out.println("Foram importados " + locais_desc.keySet().size()
				+ " locais.");
	}

	public void importarPlanilha(String inputFile) throws BiffException,IOException {

		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);

	}

	public void importarPlanilha(File inputWorkbook) throws BiffException, IOException {

		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de locais...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String local_desc = sheet.getCell(colunasList.indexOf("NUM_SALA"),
					i).getContents();

			if (locais_desc.get(local_desc) == null) {
               
				locais_desc.put(local_desc, new LocalAula(local_desc));

			}
		}
	}

}
