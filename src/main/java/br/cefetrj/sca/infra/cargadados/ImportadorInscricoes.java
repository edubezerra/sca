package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;
import br.cefetrj.sca.dominio.VersaoCurso;

/**
 * Essa classe faz a carga de objetos <code>Turma</code>.
 * 
 * @author Eduardo Bezerra
 *
 */
public class ImportadorInscricoes {

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

	public ImportadorInscricoes() {
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
				mapaTurmasVersoesCurso.clear();
				mapaTurmasCursos.clear();
				mapaAlunosNomes.clear();
				mapaAlunosCPFs.clear();
				mapaTurmasParaPeriodos.clear();
				mapaTurmasDisciplinas.clear();
				mapaTurmasAlunos.clear();
				mapaTurmasCodigos.clear();
				if (listOfFiles[i].isFile()) {
					if (!listOfFiles[i].getName().startsWith("_")) {
						System.out
								.println("Importando inscrições da planilha \""
										+ listOfFiles[i].getName());
						String arquivoPlanilha = "./planilhas/matriculas/"
								+ listOfFiles[i].getName();

						importarPlanilha(arquivoPlanilha);
						gravarDadosImportados();
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
		System.out.println("Feito!");
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

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		EntityManager em = ImportadorTudo.emf.createEntityManager();

		for (int i = 1; i < sheet.getRows(); i++) {

			String aluno_matricula = sheet.getCell(
					colunasList.indexOf("MATR_ALUNO"), i).getContents();
			/**
			 * No Moodle, o CPF do aluno é armazenado sem os pontos separadores,
			 * enquanto que os valores de CPFs provenientes do SIE possuem
			 * pontos. A instrução a seguir tem o propósito de uniformizar a
			 * representação.
			 */

			String disciplina_codigo = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String turma_codigo = sheet.getCell(
					colunasList.indexOf("COD_TURMA"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i)
					.getContents();

			String semestre_periodo = sheet.getCell(
					colunasList.indexOf("PERIODO"), i).getContents();

			int ano = Integer.parseInt(semestre_ano);
			PeriodoLetivo.EnumPeriodo periodo = null;

			if (semestre_periodo.equals("1º Semestre")) {
				periodo = EnumPeriodo.PRIMEIRO;
			} else if (semestre_periodo.equals("2º Semestre")) {
				periodo = EnumPeriodo.SEGUNDO;
			}

			/**
			 * O código não é único dentro de um período letivo. Por exemplo,
			 * pode haver várias turmas com código igual a "EXTRA" mas de
			 * disciplinas diferentes. Por conta disso, tive que montar uma
			 * chave para identificar unicamente uma turma dentro de um período
			 * letivo, conforme atribuição a seguir.
			 */
			String chaveTurma = turma_codigo + " - " + disciplina_codigo;

			PeriodoLetivo semestre = new PeriodoLetivo(ano, periodo);
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
			if (obterAlunoPorMatricula(em, matricula_aluno) == null) {
				String nome_aluno = sheet.getCell(
						colunasList.indexOf("NOME_PESSOA"), i).getContents();
				mapaAlunosNomes.put(matricula_aluno, nome_aluno);

				String cpf_aluno = sheet.getCell(colunasList.indexOf("CPF"), i)
						.getContents();
				mapaAlunosCPFs.put(matricula_aluno, cpf_aluno);
			}

		}

		em.close();
	}

	public void gravarDadosImportados() {

		EntityManager em = ImportadorTudo.emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência de objetos <code>Turma</code> e dos
		 * respectivos objetos <code>Inscricao</code>.
		 */
		Set<String> turmasIt = mapaTurmasParaPeriodos.keySet();

		int qtdInscricoes = 0;

		int qtdTurmas = 0;

		for (String chaveTurma : turmasIt) {
			PeriodoLetivo semestre = mapaTurmasParaPeriodos.get(chaveTurma);
			String codigoDisciplina = mapaTurmasDisciplinas.get(chaveTurma);
			Set<String> matriculas = mapaTurmasAlunos.get(chaveTurma);
			String numeroVersaoCurso = mapaTurmasVersoesCurso.get(chaveTurma);
			String codCurso = mapaTurmasCursos.get(chaveTurma);
			String codTurma = mapaTurmasCodigos.get(chaveTurma);

			Disciplina disciplina = obterDisciplina(em, codigoDisciplina,
					codCurso, numeroVersaoCurso);

			if (disciplina != null) {
				int capacidadeMaxima = 80;
				Turma turma = new Turma(disciplina, codTurma, capacidadeMaxima,
						semestre);

				qtdTurmas++;

				if (matriculas != null) {
					for (String matricula : matriculas) {
						Aluno aluno = obterAlunoPorMatricula(em, matricula);
						if (aluno != null) {
							try {
								turma.inscreverAluno(aluno);
							} catch (IllegalStateException e) {
								String erro = "Código turma/nome disciplina -->"
										+ turma.getCodigo()
										+ "/"
										+ turma.getNomeDisciplina();
								throw new IllegalStateException(erro, e);
							}
						} else {
							System.err
									.println("Aluno não encontrado (matrícula): "
											+ matricula
											+ ". Inserindo aluno...");
							String aluno_cpf = mapaAlunosCPFs.get(matricula);
							String aluno_nome = mapaAlunosNomes.get(matricula);

							aluno = criarAluno(aluno_nome, matricula,
									aluno_cpf, codCurso, numeroVersaoCurso, em);

							em.persist(aluno);
							System.err.println("Aluno inserido com sucesso: "
									+ aluno.toString());
						}
					}
				}

				em.persist(turma);
				qtdInscricoes += turma.getQtdInscritos();
			}
		}

		em.getTransaction().commit();

		em.close();

		System.out.println("Foram importadas " + qtdTurmas + " turmas.");

		System.out
				.println("Foram importadas " + qtdInscricoes + " inscrições.");

	}

	public Aluno criarAluno(String nomeAluno, String matriculaAluno,
			String cpfAluno, String siglaCurso, String numeroVersaoCurso,
			EntityManager em) {

		String str = "FROM VersaoCurso v WHERE v.curso.sigla = ?1 and v.numero = ?2";
		Query q = em.createQuery(str);
		q.setParameter(1, siglaCurso);
		q.setParameter(2, numeroVersaoCurso);
		VersaoCurso versaoCurso = (VersaoCurso) q.getSingleResult();
		if (versaoCurso == null) {
			throw new IllegalArgumentException(
					"Versão do curso não encontrada. Sigla: " + siglaCurso
							+ "; Versão: " + numeroVersaoCurso);
		}
		Aluno aluno = new Aluno(nomeAluno, cpfAluno, matriculaAluno,
				versaoCurso);
		return aluno;
	}

	private Aluno obterAlunoPorMatricula(EntityManager em, String matricula) {
		Query query = em
				.createQuery("from Aluno a where a.matricula = :matricula");
		query.setParameter("matricula", matricula);

		try {
			return (Aluno) query.getSingleResult();
		} catch (NoResultException ex) {
			return null;
		}
	}

	private Disciplina obterDisciplina(EntityManager em,
			String codigoDisciplina, String codCurso, String numeroVersaoCurso) {
		Query query = em
				.createQuery("from Disciplina d where d.codigo = :codigoDisciplina "
						+ "and d.versaoCurso.numero = :numeroVersaoCurso and d.versaoCurso.curso.sigla = :codCurso");
		query.setParameter("codigoDisciplina", codigoDisciplina);
		query.setParameter("codCurso", codCurso);
		query.setParameter("numeroVersaoCurso", numeroVersaoCurso);

		Disciplina disciplina = null;
		try {
			disciplina = (Disciplina) query.getSingleResult();
		} catch (NoResultException e) {
			System.err.println("Disciplina não encontrada (código - versão): "
					+ codigoDisciplina + " - " + numeroVersaoCurso);
		}
		return disciplina;

	}

}
