package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import br.cefetrj.sca.dominio.Disciplina;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorPreReqs {

	static EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

	static String colunas[] = { "COD_CURSO", "NOME_UNIDADE", "NUM_VERSAO", "DESCR_ESTRUTURA", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "COD_PRE_REQ", "NOME_PRE_REQ", "TIPO_REQUISITO", "NUM_REFERENCIA", "ITEM_TABELA",
			"ID_ESTRUTURA_CUR", "PERIODO_IDEAL" };

	public static void main(String[] args) {
		ImportadorPreReqs.run();
	}

	public static void run() {
		System.out.println("ImportadorPreReqs.run()");
		try {
			ImportadorPreReqs iim = new ImportadorPreReqs();
			String arquivoPlanilha = "./planilhas/grades-curriculares/PreReqsBCC.xls";
			iim.importarPlanilha(arquivoPlanilha);

			iim = new ImportadorPreReqs();
			arquivoPlanilha = "./planilhas/grades-curriculares/PreReqsCSTSI.xls";
			iim.importarPlanilha(arquivoPlanilha);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
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

		importarPreReqs(colunasList, sheet);
	}

	private void importarPreReqs(List<String> colunasList, Sheet sheet) {
		System.out.println("Iniciando importação de pré-requisitos de disciplinas...");
		EntityManager em = emf.createEntityManager();

		Query query = em.createQuery("from Disciplina d where d.codigo = :codigoDisciplina "
				+ "and d.versaoCurso.numero = :numeroVersaoCurso and d.versaoCurso.curso.sigla = :codCurso");

		em.getTransaction().begin();

		for (int i = 1; i < sheet.getRows(); i++) {

			String codCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numeroVersaoCurso = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			String codigoDisciplina = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
			String codigoDisciplinaPreReq = sheet.getCell(colunasList.indexOf("COD_PRE_REQ"), i).getContents();

			query.setParameter("codigoDisciplina", codigoDisciplina);
			query.setParameter("codCurso", codCurso);
			query.setParameter("numeroVersaoCurso", numeroVersaoCurso);
			Disciplina disciplina = (Disciplina) query.getSingleResult();

			query.setParameter("codigoDisciplina", codigoDisciplinaPreReq);
			query.setParameter("codCurso", codCurso);
			query.setParameter("numeroVersaoCurso", numeroVersaoCurso);

			try {
				Disciplina disciplinaPreReq = (Disciplina) query.getSingleResult();
				disciplina.comPreRequisito(disciplinaPreReq);
				System.out.println("Novo pré-requisito para disciplina " + disciplina.toString());
				System.out.println("\t" + disciplinaPreReq.toString());
			} catch (NoResultException e) {
				System.err.println("Erro ao resgatar disciplina com código " + codigoDisciplinaPreReq);
			}

			em.merge(disciplina);
		}

		em.getTransaction().commit();
	}
}
