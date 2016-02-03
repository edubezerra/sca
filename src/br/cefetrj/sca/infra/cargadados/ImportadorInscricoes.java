package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoFabrica;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;

/**
 * Essa classe faz a carga de objetos <code>Turma</code>.
 * 
 * @author Eduardo Bezerra
 *
 */
public class ImportadorInscricoes {

	String colunas[] = { "NOME_UNIDADE", "NOME_PESSOA", "CPF",
			"DT_SOLICITACAO", "DT_PROCESS", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "PERIODO_IDEAL", "PRIOR_TURMA", "PRIOR_DISC",
			"ORDEM_MATR", "SITUACAO", "COD_TURMA", "COD_CURSO", "VERSAO_CURSO",
			"MATR_ALUNO", "SITUACAO_ITEM", "ANO", "PERIODO", "IND_GERADA",
			"ID_PROCESSAMENTO", "HR_SOLICITACAO" };

	private HashMap<String, String> turmas_versoesCurso = new HashMap<>();

	private HashMap<String, String> turmas_cursos = new HashMap<>();

	private HashMap<String, String> alunos_matriculas_nomes = new HashMap<>();
	private HashMap<String, String> alunos_matriculas_cpfs = new HashMap<>();

	/**
	 * código, semestre letivo { ano, período }
	 */
	private HashMap<String, PeriodoLetivo> turmas;

	/**
	 * Dicionário de pares (código da turma, código da disciplina).
	 */
	private HashMap<String, String> turmas_disciplinas;

	/**
	 * Dicionário de pares (código da turma, {matrícula de aluno}).
	 */
	private HashMap<String, Set<String>> turmas_alunos;

	/**
	 * Apenas os cursos cujas siglas (códigos) constam nesta lista são
	 * importados.
	 */
	private static List<String> codigosCursos = new ArrayList<String>();

	private ApplicationContext context = new ClassPathXmlApplicationContext(
			new String[] { "applicationContext.xml" });

	public ImportadorInscricoes(String[] codigos) {
		if (codigos != null) {
			for (String codigo : codigos) {
				codigosCursos.add(codigo);
			}
		}
		turmas = new HashMap<>();
		turmas_disciplinas = new HashMap<>();
		turmas_alunos = new HashMap<>();
	}

	public static void mainOLD(String[] args) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");
		EntityManager em = emf.createEntityManager();
		ImportadorInscricoes importador = new ImportadorInscricoes(null);
		Disciplina disciplina = importador.obterDisciplina(em, "GTSI1263",
				"WEB", "2012");
		if (disciplina != null) {
			System.out.println(disciplina);
		}
		em.close();
	}

	public static void main(String[] args) {
		try {

			File folder = new File("./planilhas/matriculas");
			File[] listOfFiles = folder.listFiles();

			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					System.out.println("Importar dados da planlha \""
							+ listOfFiles[i].getName()
							+ "\"? Não = 0; Sim = 1.");
					Scanner in = new Scanner(System.in);
					int resposta = in.nextInt();
					if (resposta == 0) {
						// faz nada
					} else if (resposta == 1) {
						String arquivoPlanilha = "./planilhas/matriculas/"
								+ listOfFiles[i].getName();
						String codigosCursos[] = { "BCC", "WEB" };
						ImportadorInscricoes iim = new ImportadorInscricoes(
								codigosCursos);
						iim.importarPlanilha(arquivoPlanilha);
						iim.gravarDadosImportados();
					}
				} else if (listOfFiles[i].isDirectory()) {
					System.out
							.println("Diretório: " + listOfFiles[i].getName());
				}
			}
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	// public static void run(EntityManager em, String arquivoPlanilha) {
	// try {
	// String codigos[] = { "BCC", "WEB" };
	// ImportadorInscricoes iim = new ImportadorInscricoes(codigos);
	// iim.importarPlanilha(arquivoPlanilha);
	// iim.gravarDadosImportados();
	// } catch (BiffException | IOException e) {
	// e.printStackTrace();
	// System.exit(1);
	// }
	// System.out.println("Feito!");
	// }

	public void importarPlanilha(String inputFile) throws BiffException,
			IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException,
			IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out
				.println("Iniciando importação de dados relativos ao seguintes cursos: "
						+ codigosCursos.toString() + " ...");

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		for (int i = 1; i < sheet.getRows(); i++) {

			String codigoCurso = sheet.getCell(
					colunasList.indexOf("COD_CURSO"), i).getContents();

			if (!codigosCursos.contains(codigoCurso)) {
				continue;
			}

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
			PeriodoLetivo.EnumPeriodo periodo;

			if (semestre_periodo.equals("1º Semestre")) {
				periodo = EnumPeriodo.PRIMEIRO;
			} else {
				periodo = EnumPeriodo.SEGUNDO;
			}

			PeriodoLetivo semestre = new PeriodoLetivo(ano, periodo);
			turmas.put(turma_codigo, semestre);
			turmas_disciplinas.put(turma_codigo, disciplina_codigo);

			String numVersaoCurso = sheet.getCell(
					colunasList.indexOf("VERSAO_CURSO"), i).getContents();
			String siglaCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"),
					i).getContents();
			turmas_versoesCurso.put(turma_codigo, numVersaoCurso);
			turmas_cursos.put(turma_codigo, siglaCurso);

			String situacao = sheet.getCell(colunasList.indexOf("SITUACAO"), i)
					.getContents();

			if (situacao.equals("Aceita/Matriculada")) {
				if (!turmas_alunos.containsKey(turma_codigo)) {
					turmas_alunos.put(turma_codigo, new HashSet<String>());
				}

				turmas_alunos.get(turma_codigo).add(aluno_matricula);
			}

			String matricula_aluno = sheet.getCell(
					colunasList.indexOf("MATR_ALUNO"), i).getContents();
			if (obterAlunoPorMatricula(em, matricula_aluno) == null) {
				String nome_aluno = sheet.getCell(
						colunasList.indexOf("NOME_PESSOA"), i).getContents();
				alunos_matriculas_nomes.put(matricula_aluno, nome_aluno);

				String cpf_aluno = sheet.getCell(colunasList.indexOf("CPF"), i)
						.getContents();
				alunos_matriculas_cpfs.put(matricula_aluno, cpf_aluno);
			}

		}

		em.close();
	}

	public void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza a persistência de objetos <code>Turma</code> e dos
		 * respectivos objetos <code>Inscricao</code>.
		 */
		Set<String> turmasIt = turmas.keySet();
		Query query;

		int qtdInscricoes = 0;

		int qtdTurmas = 0;

		for (String codigoTurma : turmasIt) {
			PeriodoLetivo semestre = turmas.get(codigoTurma);
			String codigoDisciplina = turmas_disciplinas.get(codigoTurma);
			Set<String> matriculas = turmas_alunos.get(codigoTurma);
			String numeroVersaoCurso = turmas_versoesCurso.get(codigoTurma);
			String codCurso = turmas_cursos.get(codigoTurma);

			Disciplina disciplina = obterDisciplina(em, codigoDisciplina,
					codCurso, numeroVersaoCurso);

			if (disciplina != null) {
				int capacidadeMaxima = 80;
				Turma turma = new Turma(disciplina, codigoTurma,
						capacidadeMaxima, semestre);

				qtdTurmas++;

				if (matriculas != null) {
					for (String matricula : matriculas) {
						Aluno aluno = obterAlunoPorMatricula(em, matricula);
						if (aluno != null) {
							turma.inscreverAluno(aluno);
						} else {
							System.err
									.println("Aluno não encontrado (matrícula): "
											+ matricula
											+ ". Inserindo aluno...");
							AlunoFabrica alunoFab;
							alunoFab = (AlunoFabrica) context
									.getBean("AlunoFabricaBean");
							String aluno_cpf = alunos_matriculas_cpfs
									.get(matricula);
							String aluno_nome = alunos_matriculas_nomes
									.get(matricula);
							aluno = alunoFab.criar(aluno_nome, matricula,
									aluno_cpf, codCurso, numeroVersaoCurso);
							em.persist(aluno);
							System.err.println("Aluno inserido com sucesso: " + aluno.toString());
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
