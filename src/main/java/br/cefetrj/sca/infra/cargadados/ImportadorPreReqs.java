package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Component
public class ImportadorPreReqs {

	@Autowired
	DisciplinaRepositorio disciplinaRepositorio;

	static String colunas[] = { "COD_CURSO", "NOME_UNIDADE", "NUM_VERSAO", "DESCR_ESTRUTURA", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "COD_PRE_REQ", "NOME_PRE_REQ", "TIPO_REQUISITO", "NUM_REFERENCIA", "ITEM_TABELA",
			"ID_ESTRUTURA_CUR", "PERIODO_IDEAL" };

	public void run() {
		System.out.println("ImportadorPreReqs.run()");
		try {
			String arquivoPlanilha = "./planilhas/grades-curriculares/PreReqsBCC.xls";
			this.importarPlanilha(arquivoPlanilha);

			arquivoPlanilha = "./planilhas/grades-curriculares/PreReqsCSTSI.xls";
			this.importarPlanilha(arquivoPlanilha);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Importação de pré-requisitos de disciplinas realizada com sucesso!");
	}

	private void importarPlanilha(String inputFile) throws BiffException, IOException {
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

		for (int i = 1; i < sheet.getRows(); i++) {

			String codCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numeroVersaoCurso = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			String codigoDisciplina = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();
			String codigoDisciplinaPreReq = sheet.getCell(colunasList.indexOf("COD_PRE_REQ"), i).getContents();

			Disciplina disciplina = disciplinaRepositorio.findByCodigoEmVersaoCurso(codigoDisciplina, codCurso,
					numeroVersaoCurso);

			try {
				Disciplina disciplinaPreReq = disciplinaRepositorio.findByCodigoEmVersaoCurso(codigoDisciplinaPreReq,
						codCurso, numeroVersaoCurso);

				if (disciplinaPreReq != null) {
					disciplina.comPreRequisito(disciplinaPreReq);
					System.out.println(
							"Novo pré-requisito: " + disciplina.toString() + " <-- " + disciplinaPreReq.toString());
				} else {
					System.err.println("Pré-requisito não encontrado: " + disciplina.toString() + " <-- "
							+ codigoDisciplinaPreReq);
				}
			} catch (NoResultException e) {
				System.err.println("Erro ao resgatar disciplina com código " + codigoDisciplinaPreReq);
			}

			disciplinaRepositorio.save(disciplina);
		}
	}
}
