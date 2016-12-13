package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoFabrica;
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

/**
 * Faz a importação dos históricos escolares dos alunos.
 * 
 * Essa classe pode também realizar a inserção de objetos <code>Aluno</code> que
 * esteja na planilha, mas que ainda não esteja no mecanismo persistente. Nesse
 * caso, a inserção é realizada apenas se houver CPF definido para esse aluno na
 * planilha usada como entrada.
 * 
 * @author Eduardo Bezerra
 *
 */
@Component
public class ImportadorHistoricosEscolares {

	@Autowired
	DisciplinaRepositorio disciplinaRepositorio;

	@Autowired
	AlunoRepositorio alunoRepositorio;

	@Autowired
	private AlunoFabrica alunoFabrica;

	static String colunas[] = { "COD_CURSO", "CURSO", "VERSAO_CURSO", "CPF",
			"MATR_ALUNO", "NOME_PESSOA", "FORMA_EVASAO", "COD_TURMA",
			"COD_DISCIPLINA", "NOME_DISCIPLINA", "ANO", "PERIODO", "SITUACAO",
			"CH_TOTAL", "CREDITOS", "MEDIA_FINAL", "NUM_FALTAS" };

	public void run() {
		File folder = new File("./planilhas/historicos-escolares");
		File[] listOfFiles = folder.listFiles();

		Scanner in = new Scanner(System.in);
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				int resposta = 1;
				if (resposta == 1) {
					String arquivoPlanilha = "./planilhas/historicos-escolares/"
							+ listOfFiles[i].getName();
					run(arquivoPlanilha);
				}
			} else if (listOfFiles[i].isDirectory()) {
				System.out.println("Diretório: " + listOfFiles[i].getName());
			}
		}
		in.close();
	}

	public void run(String planilha) {
		System.out.println("ImportadorHistoricosEscolares.run()");
		try {
			importarPlanilha(planilha);
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	private void importarPlanilha(String inputFile) throws BiffException,
			IOException {
		File inputWorkbook = new File(inputFile);
		String resultado = importarPlanilha(inputWorkbook);
		System.out.println(resultado);
	}

	public String importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		StringBuilder response = new StringBuilder();
		int repetido = 0, importado = 0, erro = 0, codigoTrancamento = 0;
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		response.append("Iniciando importação de históricos escolares...;");

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
			String aluno_nome = sheet.getCell(
					colunasList.indexOf("NOME_PESSOA"), i).getContents();
			String aluno_cpf = sheet.getCell(colunasList.indexOf("CPF"), i)
					.getContents();

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
			} else if (situacao.equals("Aprovado sem nota")) {
				situacaoFinal = EnumSituacaoAvaliacao.APROVADO_SEM_NOTA;
			} else if (situacao.equals("Reprovado com Dependencia")) {
				situacaoFinal = EnumSituacaoAvaliacao.REAPROVADO_COM_DEPENDENCIA;
			} else {
				response.append("ERRO GRAVE: Valor inválido para a situação final de avaliação! "
						+ situacao + ";");
				return response.toString();
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

			Aluno aluno = alunoRepositorio.findAlunoByInfoHistoricoEscolar(
					aluno_matricula, semestre, situacaoFinal, cod_disciplina,
					versao_curso, cod_curso);

			if (aluno != null) {
				repetido++;
				continue;
			} else {
				Disciplina disciplina = disciplinaRepositorio
						.findByCodigoEmVersaoCurso(cod_disciplina, cod_curso,
								versao_curso);

				if (disciplina != null) {
					aluno = alunoRepositorio
							.findAlunoByMatricula(aluno_matricula);

					if (aluno == null) {
						if (aluno_cpf != null && !aluno_cpf.isEmpty()) {
							System.err
									.println("Criando novo aluno. Matrícula: "
											+ aluno_matricula);
							aluno = alunoFabrica.criar(aluno_nome,
									aluno_matricula, aluno_cpf, cod_curso,
									versao_curso);
						} else {
							System.err
									.println("Aluno sem CPF; não pode ser registrado. Matrícula: "
											+ aluno_matricula);
							erro++;
						}
					}

					if (aluno != null) {
						importado++;
						aluno.registrarNoHistoricoEscolar(disciplina,
								situacaoFinal, semestre);
						alunoRepositorio.save(aluno);

						System.out
								.println("Lançada disciplina "
										+ disciplina.toString()
										+ " no histórico escolar do aluno de matrícula "
										+ aluno.getMatricula());
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
