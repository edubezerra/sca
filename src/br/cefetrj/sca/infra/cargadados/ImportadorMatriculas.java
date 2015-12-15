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

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.EnumSituacaoFinalAvaliacao;
import br.cefetrj.sca.dominio.HistoricoEscolar;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;

/**
 * @author Eduardo Bezerra
 *
 */
public class ImportadorMatriculas {

	public ImportadorMatriculas(String[] codigosCursos) {
	}

	public static void main(String[] args) {
		String planilha = "./planilhas/matriculas/Matrícula-DEPIN-2015-2.xls";
		ImportadorMatriculas.run(planilha);
	}

	public static void run(String planilha) {
		System.out.println("ImportadorHistoricosEscolares.main()");
		try {
			String codigosCursos[] = { "BCC" };
			ImportadorMatriculas iim = new ImportadorMatriculas(codigosCursos);
			iim.importarPlanilha(planilha);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	NOME_UNIDADE	NOME_PESSOA	DT_SOLICITACAO	DT_PROCESS	COD_DISCIPLINA	NOME_DISCIPLINA	PERIODO_IDEAL	PRIOR_TURMA	PRIOR_DISC	ORDEM_MATR	SITUACAO	COD_TURMA	COD_CURSO	MATR_ALUNO	SITUACAO_ITEM	ANO	PERIODO	IND_GERADA	ID_PROCESSAMENTO	HR_SOLICITACAO
	
	static String colunas[] = { "MATR_ALUNO", "NOME_PESSOA", "FORMA_EVASAO", "COD_TURMA", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "ANO", "PERIODO", "SITUACAO", "CH_TOTAL", "CREDITOS", "MEDIA_FINAL", "NUM_FALTAS" };

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de históricos escolares...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		EntityManagerFactory emf = Persistence.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		for (int i = 1; i < sheet.getRows(); i++) {

			String aluno_matricula = sheet.getCell(colunasList.indexOf("MATR_ALUNO"), i).getContents();

			String disciplina_codigo = sheet.getCell(colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i).getContents();

			String semestre_periodo = sheet.getCell(colunasList.indexOf("PERIODO"), i).getContents();

			int ano = Integer.parseInt(semestre_ano);
			SemestreLetivo.EnumPeriodo periodo;

			if (semestre_periodo.equals("1º Semestre")) {
				periodo = EnumPeriodo.PRIMEIRO;
			} else {
				periodo = EnumPeriodo.SEGUNDO;
			}

			SemestreLetivo semestre = new SemestreLetivo(ano, periodo);

			String situacao = sheet.getCell(colunasList.indexOf("SITUACAO"), i).getContents();
			EnumSituacaoFinalAvaliacao situacaoFinal = null;
			if (situacao.equals("Aprovado")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.APROVADO;
			} else if (situacao.equals("Reprovado")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.REPROVADO_POR_MEDIA;
			} else if (situacao.equals("Reprovado por Frequência")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.REPROVADO_POR_FALTAS;
			} else if (situacao.equals("Trancamento de Disciplinas")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.TRANCAMENTO_DISCIPLINA;
			} else if (situacao.equals("Matrícula")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.INDEFINIDA;
			} else if (situacao.equals("Isento por Transferência")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.ISENTO_POR_TRANSFERENCIA;
			} else if (situacao.equals("Trancamento Total")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.TRANCAMENTO_TOTAL;
			} else if (situacao.equals("Aproveitamento por Estudos")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.APROVEITAMENTO_POR_ESTUDOS;
			} else if (situacao.equals("Aproveitamento de Créditos")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.APROVEITAMENTO_CREDITOS;
			} else if (situacao.equals("Isento")) {
				situacaoFinal = EnumSituacaoFinalAvaliacao.ISENTO;
			} else {
				System.err.println("ERRO GRAVE: Valor inválido para a situação final de avaliação! " + situacao);
				System.exit(1);
			}

			if (disciplina_codigo.equals("TRT001")) {
				/**
				 * Esse código representa trancamento do período.
				 */
				continue;
			}

			try {
				Query queryDisciplina;
				queryDisciplina = em.createQuery("from Disciplina a where a.codigo = :codigoDisciplina and "
						+ "a.versaoCurso.numero = :numeroVersao and " + "a.versaoCurso.curso.sigla = :siglaCurso");
				queryDisciplina.setParameter("codigoDisciplina", disciplina_codigo);

				// TODO: generalizar para outros cursos.
				queryDisciplina.setParameter("numeroVersao", "2012");
				queryDisciplina.setParameter("siglaCurso", "BCC");

				Disciplina disciplina = (Disciplina) queryDisciplina.getSingleResult();
				try {
					Query queryAluno;
					queryAluno = em.createQuery("from Aluno a where a.matricula = :matricula");
					queryAluno.setParameter("matricula", aluno_matricula);
					Aluno aluno = (Aluno) queryAluno.getSingleResult();
					HistoricoEscolar he = aluno.getHistorico();
					he.lancar(disciplina, situacaoFinal, semestre);
					em.merge(he);
					System.out.println("Lançada disciplina " + disciplina.getNome() + " para aluno " + aluno.getMatricula());
				} catch (NoResultException e) {
					System.err.println("Aluno não encontrado. Matrícula: " + aluno_matricula);
				}
			} catch (NoResultException e) {
				System.err.println("Disciplina não encontrada. Código: " + disciplina_codigo);
			}
		}
		em.getTransaction().commit();

		em.close();

		System.out.println("Importação de históricos finalizada.");
	}

}
