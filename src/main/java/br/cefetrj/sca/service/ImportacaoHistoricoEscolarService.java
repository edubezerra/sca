package br.cefetrj.sca.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.EnumSituacaoAvaliacao;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Component
public class ImportacaoHistoricoEscolarService {

	@Autowired
	AlunoRepositorio alunoRepo;

	@Autowired
	DisciplinaRepositorio disciplinaRepo;

	private static String colunas[] = { "COD_CURSO", "CURSO", "VERSAO_CURSO",
			"CPF", "MATR_ALUNO", "NOME_PESSOA", "FORMA_EVASAO", "COD_TURMA",
			"COD_DISCIPLINA", "NOME_DISCIPLINA", "ANO", "PERIODO", "SITUACAO",
			"CH_TOTAL", "CREDITOS", "MEDIA_FINAL", "NUM_FALTAS" };

	public String importaHistoricoEscolar(MultipartFile file) {
		String response = null;
		try {
			File tempFile = File.createTempFile("import-historico", "");
			file.transferTo(tempFile);

			response = importarPlanilha(tempFile);

		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return "Erro ao importar a planilha do histórico escolar.";
		}

		return response;
	}

	private String importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		StringBuilder response = new StringBuilder();
		int repetido = 0, importado = 0, erro = 0, codigoTrancamento = 0;
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Iniciando importação de históricos escolares...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String cod_curso = sheet.getCell(colunasList.indexOf("COD_CURSO"),
					i).getContents();

			String versao_curso = sheet.getCell(
					colunasList.indexOf("VERSAO_CURSO"), i).getContents();

			String aluno_matricula = sheet.getCell(
					colunasList.indexOf("MATR_ALUNO"), i).getContents();

			String cod_disciplina = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i)
					.getContents();

			String semestre_periodo = sheet.getCell(
					colunasList.indexOf("PERIODO"), i).getContents();

			int ano = Integer.parseInt(semestre_ano);
			PeriodoLetivo.EnumPeriodo periodo;

			if (semestre_periodo.equals("1º Semestre")) {
				periodo = EnumPeriodo.PRIMEIRO;
			} else {
				periodo = EnumPeriodo.SEGUNDO;
			}

			PeriodoLetivo semestre = new PeriodoLetivo(ano, periodo);

			String situacao = sheet.getCell(colunasList.indexOf("SITUACAO"), i)
					.getContents();

			EnumSituacaoAvaliacao situacaoFinal = null;
			if (situacao.equals("Aprovado")) {
				situacaoFinal = EnumSituacaoAvaliacao.APROVADO;
			} else if (situacao.equals("Reprovado")) {
				situacaoFinal = EnumSituacaoAvaliacao.REPROVADO_POR_MEDIA;
			} else if (situacao.equals("Reprovado por Frequência")) {
				situacaoFinal = EnumSituacaoAvaliacao.REPROVADO_POR_FALTAS;
			} else if (situacao.equals("Reprovado sem nota")) {
				situacaoFinal = EnumSituacaoAvaliacao.REPROVADO_SEM_NOTA;
			} else if (situacao.equals("Trancamento de Disciplinas")) {
				situacaoFinal = EnumSituacaoAvaliacao.TRANCAMENTO_DISCIPLINA;
			} else if (situacao.equals("Matrícula")) {
				situacaoFinal = EnumSituacaoAvaliacao.INDEFINIDA;
			} else if (situacao.equals("Isento por Transferência")) {
				situacaoFinal = EnumSituacaoAvaliacao.ISENTO_POR_TRANSFERENCIA;
			} else if (situacao.equals("Trancamento Total")) {
				situacaoFinal = EnumSituacaoAvaliacao.TRANCAMENTO_TOTAL;
			} else if (situacao.equals("Aproveitamento por Estudos")) {
				situacaoFinal = EnumSituacaoAvaliacao.APROVEITAMENTO_POR_ESTUDOS;
			} else if (situacao.equals("Aproveitamento de Créditos")) {
				situacaoFinal = EnumSituacaoAvaliacao.APROVEITAMENTO_CREDITOS;
			} else if (situacao.equals("Isento")) {
				situacaoFinal = EnumSituacaoAvaliacao.ISENTO;
			} else {
				System.err
						.println("ERRO GRAVE: Valor inválido para a situação final de avaliação! "
								+ situacao);
				System.exit(1);
			}

			if (cod_disciplina.equals("TRT001")) {
				codigoTrancamento++;
				/**
				 * Esse código representa trancamento do período.
				 * 
				 * TODO: implementar o registro dessa situação no histórico
				 * escolar do aluno.
				 */
				continue;
			}

			Aluno aluno = alunoRepo.getAlunoByInfoHistoricoEscolar(
					aluno_matricula, semestre, situacaoFinal, cod_disciplina,
					versao_curso, cod_curso);

			if (aluno != null) {
				repetido++;
				continue;
			} else {
				Disciplina disciplina = disciplinaRepo
						.findByCodigoEmVersaoCurso(cod_disciplina, cod_curso,
								versao_curso);

				if (disciplina != null) {
					aluno = alunoRepo.findAlunoByMatricula(aluno_matricula);

					if (aluno != null) {
						importado++;
						aluno.registrarNoHistoricoEscolar(disciplina,
								situacaoFinal, semestre);
						alunoRepo.save(aluno);

						System.out
								.println("Lançada disciplina "
										+ disciplina.toString()
										+ " no histórico escolar do aluno de matrícula "
										+ aluno.getMatricula());
					} else {
						erro++;
						System.err.println("Aluno não encontrado. Matrícula: "
								+ aluno_matricula);
					}
				} else {
					erro++;
					System.err.println("Disciplina não encontrada (código): "
							+ cod_disciplina);
				}
			}
		}

		response.append("Importação de históricos finalizada.;");
		response.append("Quantidade total de registros da planilha: "
				+ (sheet.getRows() - 1) + ";");
		response.append("Quantidade repetida: " + repetido + ";");
		response.append("Quantidade importada: " + importado + ";");
		response.append("Quantidade não importada por erros: " + erro + ";");
		response.append("Quantidade de códigos de trancamento: "
				+ codigoTrancamento);

		return response.toString();
	}
}
