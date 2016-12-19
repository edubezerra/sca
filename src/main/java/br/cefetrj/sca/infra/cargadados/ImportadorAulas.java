package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import br.cefetrj.sca.dominio.Aula;
import br.cefetrj.sca.dominio.EnumDiaSemana;
import br.cefetrj.sca.dominio.LocalAula;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class ImportadorAulas {

	String colunas[] = { "COD_CURSO", "COD_DISCIPLINA", "COD_TURMA",
			"PERIODO_IDEAL", "NOME_DISCIPLINA", "DIA_SEMANA", "HR_INICIO",
			"HR_FIM", "TIPO_AULA", "NOME_UNIDADE", "ANO", "PERIODO", "NUM_SALA" };

	String siglas_dias[] = { "DOM", "SEG", "TER", "QUA", "QUI", "SEX", "SAB" };

	private HashMap<String, Aula> turma_aulas = new HashMap<>();

	public ImportadorAulas() {

	}

	public void run() {

		System.out.println("ImportadorAula.run()");

		try {
			String planilhaAulas = "./planilhas/GRADUACAO.SALAS.2014.2.MARACANA.xls";
			this.importarPlanilha(planilhaAulas);
			this.gravarDadosImportados();

		} catch (BiffException | IOException e) {

			e.printStackTrace();
			System.exit(1);

		}
		System.out.println("Feito!");
	}

	private void gravarDadosImportados() {

//		Set<String> aulasIt = turma_aulas.keySet();
//
//		for (String cod_turma : aulasIt) {
//
//			Query query;
//			Turma turma = null;
//
//			try {
//				query = em
//						.createQuery("from Turma t where t.codigo = :codigoTurma");
//				query.setParameter("codigoTurma", cod_turma);
//				turma = (Turma) query.getSingleResult();
//			} catch (NoResultException e) {
//				System.out.println("Turma nao encontrada: " + cod_turma);
//				turma = null;
//			}
//
//			if (turma != null) {
//				Aula aula = turma_aulas.get(cod_turma);
//				try {
//					query = em
//							.createQuery("from LocalAula la where la.descricao = :descricaoTurma");
//					query.setParameter("descricaoTurma", aula.getLocal()
//							.getDescricao());
//					LocalAula local = (LocalAula) query.getSingleResult();
//					turma.adicionarAula(aula.getDia().toString(),
//							aula.getHoraInicio(), aula.getHoraTermino(), local);
//				} catch (NoResultException e) {
//					System.out.println("Local não encontrado: "
//							+ aula.getLocal().getDescricao());
//					turma.adicionarAula(aula.getDia().toString(),
//							aula.getHoraInicio(), aula.getHoraTermino(), null);
//				}
//				System.out.println("Atualizando turma de código "
//						+ turma.getCodigo());
//				em.merge(turma);
//			}
//		}
//
//		em.getTransaction().commit();
//
//		em.close();
	}

	public void importarPlanilha(String inputFile) throws BiffException,
			IOException {

		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);

	}

	public void importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {

		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de aulas...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String cod_turma = sheet.getCell(colunasList.indexOf("COD_TURMA"),
					i).getContents();

			String dia_semana = sheet.getCell(
					colunasList.indexOf("DIA_SEMANA"), i).getContents();

			String str_inicio = sheet.getCell(colunasList.indexOf("HR_INICIO"),
					i).getContents();

			String str_fim = sheet.getCell(colunasList.indexOf("HR_FIM"), i)
					.getContents();

			String num_sala = sheet.getCell(colunasList.indexOf("NUM_SALA"), i)
					.getContents();

			EnumDiaSemana dia = null;

			// Verificando se os horários não são Strings vazias.
			if (str_inicio != "" && str_fim != "") {

				// comparando os dias da semana da planilha e atribuindo seu
				// valor EnumDiaSemana correspondente.
				for (int j = 0; j < EnumDiaSemana.dias().size(); j++) {

					if (dia_semana.equals(siglas_dias[j])) {
						dia = EnumDiaSemana.findByText(EnumDiaSemana.dias()
								.get(j));
					}
				}

				LocalAula local = new LocalAula(num_sala);

				Aula aula = new Aula(dia, str_inicio, str_fim, local);

				turma_aulas.put(cod_turma, aula);

			}

		}
	}

}
