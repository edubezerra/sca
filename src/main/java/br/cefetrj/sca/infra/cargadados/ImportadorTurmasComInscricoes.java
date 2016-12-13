package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoFabrica;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.dominio.repositories.TurmaRepositorio;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 * Esse importador faz a carga de objetos <code>Turma</code> e de seus
 * respectivos objetos <code>Inscricao</code> para o repositório de objetos.
 * 
 * Esse importador também faz a carga de objetos <code>Aluno</code> que estejam
 * na planilha de entrada, mas que não ainda constam no repositório de objetos.
 * 
 * @author Eduardo Bezerra
 *
 */
@Component
public class ImportadorTurmasComInscricoes {

	@Autowired
	AlunoRepositorio alunoRepositorio;

	@Autowired
	DisciplinaRepositorio disciplinaRepositorio;

	@Autowired
	private TurmaRepositorio turmaRepositorio;

	@Autowired
	private AlunoFabrica alunoFabrica;

	private final String colunas[] = { "NOME_UNIDADE", "NOME_PESSOA", "CPF",
			"DT_SOLICITACAO", "DT_PROCESS", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "PERIODO_IDEAL", "PRIOR_TURMA", "PRIOR_DISC",
			"ORDEM_MATR", "SITUACAO", "COD_TURMA", "COD_CURSO", "VERSAO_CURSO",
			"MATR_ALUNO", "PERIODO_ATUAL", "SITUACAO_ITEM", "ANO", "PERIODO",
			"IND_GERADA", "ID_PROCESSAMENTO", "HR_SOLICITACAO" };

	/**
	 * Dicionário turmas --> versões de curso.
	 * 
	 * chave: codigo da turma + código da disciplina,
	 * 
	 * valor: código da versão do curso
	 */
	private HashMap<String, String> mapaTurmasVersoesCurso;

	/**
	 * Dicionário turmas --> versões de curso.
	 * 
	 * chave: codigo da turma + código da disciplina,
	 * 
	 * valor: sigla do curso
	 */
	private HashMap<String, String> mapaTurmasCursos;

	/**
	 * Dicionário alunos --> nomes.
	 * 
	 * chave: matrícula do aluno,
	 * 
	 * valor: nome do aluno
	 */
	private HashMap<String, String> mapaAlunosNomes;

	/**
	 * Dicionário alunos --> CPFs.
	 * 
	 * chave: matrícula do aluno,
	 * 
	 * valor: CPF do aluno
	 */
	private HashMap<String, String> mapaAlunosCPFs;

	/**
	 * Dicionário turmas --> períodos letivos.
	 * 
	 * chave: codigo da turma + código da disciplina,
	 * 
	 * valor: período letivo { ano, período }
	 */
	private HashMap<String, PeriodoLetivo> mapaTurmasParaPeriodos;

	/**
	 * Dicionário turmas --> disciplinas.
	 * 
	 * chave: codigo da turma + código da disciplina,
	 * 
	 * valor: código da disciplina
	 */
	private HashMap<String, String> mapaTurmasDisciplinas;

	/**
	 * Dicionário turmas --> alunos.
	 * 
	 * chave: codigo da turma + código da disciplina,
	 * 
	 * valor: matrícula do aluno
	 */
	private HashMap<String, Set<String>> mapaTurmasAlunos;

	/**
	 * Dicionário turmas --> códigos das turmas.
	 * 
	 * chave: codigo da turma + código da disciplina,
	 * 
	 * valor: código da turma
	 */
	private HashMap<String, String> mapaTurmasCodigos;

	public ImportadorTurmasComInscricoes() {
		mapaTurmasVersoesCurso = new HashMap<>();
		mapaTurmasCursos = new HashMap<>();
		mapaAlunosNomes = new HashMap<>();
		mapaAlunosCPFs = new HashMap<>();
		mapaTurmasParaPeriodos = new HashMap<>();
		mapaTurmasDisciplinas = new HashMap<>();
		mapaTurmasAlunos = new HashMap<>();
		mapaTurmasCodigos = new HashMap<>();
	}

	public void run() {
		try {
			File folder = new File("./planilhas/matriculas");
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					if (!listOfFiles[i].getName().startsWith("_")) {
						System.out
								.println("Importando inscrições da planilha \""
										+ listOfFiles[i].getName());
						String arquivoPlanilha = "./planilhas/matriculas/"
								+ listOfFiles[i].getName();

						importarPlanilha(arquivoPlanilha);
					}
				} else if (listOfFiles[i].isDirectory()) {
					System.out
							.println("Diretório: " + listOfFiles[i].getName());
				}
			}
		} catch (BiffException | IOException e) {
			System.err.println("Erro fatal!");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public void importarPlanilha(String inputFile) throws BiffException,
			IOException {

		reiniciarEstruturasArmazenamento();

		File inputWorkbook = new File(inputFile);
		String mensagens = importarPlanilha(inputWorkbook);
		System.out.println(mensagens);
	}

	private void reiniciarEstruturasArmazenamento() {
		mapaTurmasVersoesCurso.clear();
		mapaTurmasCursos.clear();
		mapaAlunosNomes.clear();
		mapaAlunosCPFs.clear();
		mapaTurmasParaPeriodos.clear();
		mapaTurmasDisciplinas.clear();
		mapaTurmasAlunos.clear();
		mapaTurmasCodigos.clear();
	}

	public String importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		StringBuilder response = new StringBuilder();
		Workbook w;

		reiniciarEstruturasArmazenamento();

		List<String> colunasList = Arrays.asList(colunas);

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String aluno_matricula = sheet.getCell(
					colunasList.indexOf("MATR_ALUNO"), i).getContents();

			String disciplina_codigo = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String turma_codigo = sheet.getCell(
					colunasList.indexOf("COD_TURMA"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i)
					.getContents();

			String semestre_periodo = sheet.getCell(
					colunasList.indexOf("PERIODO"), i).getContents();

			PeriodoLetivo semestre = UtilsImportacao.getPeriodoLetivo(
					semestre_ano, semestre_periodo);

			/**
			 * O código associado a uma turma não é único dentro de um período
			 * letivo. Por exemplo, pode haver várias turmas com código igual a
			 * "EXTRA" mas de disciplinas diferentes. Por conta disso, tive que
			 * montar uma chave para identificar unicamente uma turma dentro de
			 * um período letivo, conforme atribuição a seguir.
			 */
			String chaveTurma = turma_codigo + " - " + disciplina_codigo;

			mapaTurmasParaPeriodos.put(chaveTurma, semestre);
			mapaTurmasDisciplinas.put(chaveTurma, disciplina_codigo);

			mapaTurmasCodigos.put(chaveTurma, turma_codigo);

			String numVersaoCurso = sheet.getCell(
					colunasList.indexOf("VERSAO_CURSO"), i).getContents();
			String siglaCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"),
					i).getContents();
			mapaTurmasVersoesCurso.put(chaveTurma, numVersaoCurso);
			mapaTurmasCursos.put(chaveTurma, siglaCurso);

			String situacao = sheet.getCell(colunasList.indexOf("SITUACAO"), i)
					.getContents();

			if (situacao.equals("Aceita/Matriculada")) {
				if (!mapaTurmasAlunos.containsKey(chaveTurma)) {
					mapaTurmasAlunos.put(chaveTurma, new HashSet<String>());
				}
				mapaTurmasAlunos.get(chaveTurma).add(aluno_matricula);
			}

			String matricula_aluno = sheet.getCell(
					colunasList.indexOf("MATR_ALUNO"), i).getContents();

			Aluno aluno = alunoRepositorio
					.findAlunoByMatricula(matricula_aluno);
			if (aluno == null) {
				String nome_aluno = sheet.getCell(
						colunasList.indexOf("NOME_PESSOA"), i).getContents();
				mapaAlunosNomes.put(matricula_aluno, nome_aluno);

				String cpf_aluno = sheet.getCell(colunasList.indexOf("CPF"), i)
						.getContents();
				mapaAlunosCPFs.put(matricula_aluno, cpf_aluno);
			}

		}

		response = gravarDadosImportados(response);

		return response.toString();
	}

	public StringBuilder gravarDadosImportados(StringBuilder response) {
		/**
		 * Realiza a persistência de objetos <code>Turma</code> e dos
		 * respectivos objetos <code>Inscricao</code>.
		 */
		Set<String> turmasIt = mapaTurmasParaPeriodos.keySet();

		int qtdInscricoes = 0;

		int qtdTurmas = 0;

		int qtdAlunos = 0;

		for (String chaveTurma : turmasIt) {
			PeriodoLetivo periodo = mapaTurmasParaPeriodos.get(chaveTurma);
			String codigoDisciplina = mapaTurmasDisciplinas.get(chaveTurma);
			Set<String> matriculas = mapaTurmasAlunos.get(chaveTurma);
			String numeroVersaoCurso = mapaTurmasVersoesCurso.get(chaveTurma);
			String codCurso = mapaTurmasCursos.get(chaveTurma);
			String codTurma = mapaTurmasCodigos.get(chaveTurma);

			Disciplina disciplina = disciplinaRepositorio
					.findByCodigoEmVersaoCurso(codigoDisciplina, codCurso,
							numeroVersaoCurso);

			if (disciplina != null) {
				int capacidadeMaxima = 100;

				Turma turma = turmaRepositorio
						.findTurmaByCodigoAndDisciplinaAndPeriodo(codTurma,
								disciplina, periodo);
				if (turma == null) {
					turma = new Turma(disciplina, codTurma, capacidadeMaxima,
							periodo);
					qtdTurmas++;
				}

				if (matriculas != null) {
					for (String matricula : matriculas) {
						Aluno aluno = alunoRepositorio
								.findAlunoByMatricula(matricula);
						if (aluno == null) {
							String aluno_cpf = mapaAlunosCPFs.get(matricula);
							String aluno_nome = mapaAlunosNomes.get(matricula);

							aluno = alunoFabrica.criar(aluno_nome, matricula,
									aluno_cpf, codCurso, numeroVersaoCurso);

							alunoRepositorio.save(aluno);

							qtdAlunos++;
						}
						try {
							if (!turma.isAlunoInscrito(aluno)) {
								turma.inscreverAluno(aluno);
								qtdInscricoes++;
							}
						} catch (IllegalStateException e) {
							response.append("Importação não pode ser realizada!; ");
							response.append("Código turma/nome disciplina -->"
									+ turma.getCodigo() + "/"
									+ turma.getNomeDisciplina() + ";");
							response.append("Erro: " + e.getMessage());
							return response;
						}
					}
				}

				turmaRepositorio.save(turma);
			}
		}

		response.append("Resumo da importação:\n;");
		response.append(qtdTurmas + " turmas importadas;");
		response.append(qtdInscricoes + " inscrições importadas;");
		response.append(qtdAlunos + " alunos importados;");

		return response;
	}

	// public Aluno criarAluno(String nomeAluno, String matriculaAluno,
	// String cpfAluno, String siglaCurso, String numeroVersaoCurso) {
	// VersaoCurso versaoCurso = cursoRepositorio.getVersaoCurso(siglaCurso,
	// numeroVersaoCurso);
	//
	// if (versaoCurso == null) {
	// throw new IllegalArgumentException(
	// "Versão do curso não encontrada. Sigla: " + siglaCurso
	// + "; Versão: " + numeroVersaoCurso);
	// }
	// Aluno aluno = new Aluno(nomeAluno, cpfAluno, matriculaAluno,
	// versaoCurso);
	// return aluno;
	// }
}
