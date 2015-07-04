package br.cefetrj.sca.infra.autoavaliacao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;
import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.SemestreLetivo;
import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;
import br.cefetrj.sca.dominio.Turma;

/**
 * Essa classe faz a carga de dados de uma planilha para o banco de dados do
 * SCA.
 * 
 * Os dados importados são sobre:
 * 
 * (1) disciplinas,
 * 
 * (2) turmas,
 * 
 * (3) alunos e
 * 
 * (4) inscrições de alunos em turmas.
 * 
 * @author Eduardo Bezerra
 *
 */
public class ImportadorInscricoes {

	/**
	 * Dicionário de pares (matrícula, nome) de cada aluno.
	 */
	private HashMap<String, String> alunos_nomes;

	/**
	 * Dicionário de pares (matrícula, CPF) de cada aluno.
	 */
	private HashMap<String, String> alunos_cpfs;

	/**
	 * Dicionário de pares (código, nome) de cada aluno.
	 */
	private HashMap<String, String> disciplinas;

	/**
	 * código, semestre letivo { ano, período }
	 */
	private HashMap<String, SemestreLetivo> turmas;

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
	private List<String> codigosCursos = new ArrayList<String>();

	public ImportadorInscricoes(String[] codigosCursos) {
		this.codigosCursos = Arrays.asList(codigosCursos);
		alunos_nomes = new HashMap<>();
		alunos_cpfs = new HashMap<>();
		disciplinas = new HashMap<>();
		turmas = new HashMap<>();
		turmas_disciplinas = new HashMap<>();
		turmas_alunos = new HashMap<>();
	}

	public static void run(EntityManager em, String arquivoPlanilha) {
		System.out.println("ImportadorInformacoesMatricula.main()");
		try {
			String codigosCursos[] = { "BCC", "WEB" };
			ImportadorInscricoes iim = new ImportadorInscricoes(codigosCursos);
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	String colunas[] = { "NOME_UNIDADE", "NOME_PESSOA", "CPF",
			"DT_SOLICITACAO", "DT_PROCESS", "COD_DISCIPLINA",
			"NOME_DISCIPLINA", "PERIODO_IDEAL", "PRIOR_TURMA", "PRIOR_DISC",
			"ORDEM_MATR", "SITUACAO", "COD_TURMA", "COD_CURSO", "MATR_ALUNO",
			"SITUACAO_ITEM", "ANO", "PERIODO", "IND_GERADA",
			"ID_PROCESSAMENTO", "HR_SOLICITACAO" };

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

		for (int i = 1; i < sheet.getRows(); i++) {

			String codigoCurso = sheet.getCell(
					colunasList.indexOf("COD_CURSO"), i).getContents();

			if (!codigosCursos.contains(codigoCurso)) {
				continue;
			}

			String aluno_matricula = sheet.getCell(
					colunasList.indexOf("MATR_ALUNO"), i).getContents();
			String aluno_nome = sheet.getCell(
					colunasList.indexOf("NOME_PESSOA"), i).getContents();

			String aluno_cpf = sheet.getCell(colunasList.indexOf("CPF"), i)
					.getContents();
			/**
			 * No Moodle, o CPF do aluno é armazenado sem os pontos separadores,
			 * enquanto que os valores de CPFs provenientes do SIE possuem
			 * pontos. A instrução a seguir tem o propósito de uniformizar a
			 * representação.
			 */
			aluno_cpf = aluno_cpf.replace(".", "");

			alunos_nomes.put(aluno_matricula, aluno_nome);
			alunos_cpfs.put(aluno_matricula, aluno_cpf);

			String disciplina_codigo = sheet.getCell(
					colunasList.indexOf("COD_DISCIPLINA"), i).getContents();

			String disciplina_nome = sheet.getCell(
					colunasList.indexOf("NOME_DISCIPLINA"), i).getContents();

			disciplinas.put(disciplina_codigo, disciplina_nome);

			String turma_codigo = sheet.getCell(
					colunasList.indexOf("COD_TURMA"), i).getContents();

			String semestre_ano = sheet.getCell(colunasList.indexOf("ANO"), i)
					.getContents();

			String semestre_periodo = sheet.getCell(
					colunasList.indexOf("PERIODO"), i).getContents();

			int ano = Integer.parseInt(semestre_ano);
			SemestreLetivo.EnumPeriodo periodo;

			if (semestre_periodo.equals("1º Semestre")) {
				periodo = EnumPeriodo.PRIMEIRO;
			} else {
				periodo = EnumPeriodo.SEGUNDO;
			}

			SemestreLetivo semestre = new SemestreLetivo(ano, periodo);
			turmas.put(turma_codigo, semestre);
			turmas_disciplinas.put(turma_codigo, disciplina_codigo);

			String situacao = sheet.getCell(colunasList.indexOf("SITUACAO"), i)
					.getContents();

			if (situacao.equals("Aceita/Matriculada")) {
				if (!turmas_alunos.containsKey(turma_codigo)) {
					turmas_alunos.put(turma_codigo, new HashSet<String>());
				}

				turmas_alunos.get(turma_codigo).add(aluno_matricula);
			}
		}
		System.out.println("Importação finalizada.");
	}

	public void gravarDadosImportados() {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory("SCAPU");

		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		/**
		 * Realiza persistências de objetos Aluno.
		 */
		Set<String> alunosIt = alunos_nomes.keySet();
		for (String matricula : alunosIt) {
			em.persist(new Aluno(alunos_nomes.get(matricula), matricula,
					alunos_cpfs.get(matricula)));
		}

		/**
		 * Realiza a persistência de objetos <code>Disciplina</code>.
		 */
		Set<String> disciplinasIt = disciplinas.keySet();
		for (String codigoDisciplina : disciplinasIt) {
			em.persist(new Disciplina(disciplinas.get(codigoDisciplina),
					codigoDisciplina, 4));
		}

		/**
		 * Realiza a persistência de objetos <code>Turma</code> e dos
		 * respectivos objetos <code>Inscricao</code>.
		 */
		Set<String> turmasIt = turmas.keySet();
		Query query;

		int qtdInscricoes = 0;

		for (String codigoTurma : turmasIt) {
			SemestreLetivo semestre = turmas.get(codigoTurma);
			String codigoDisciplina = turmas_disciplinas.get(codigoTurma);
			Set<String> matriculas = turmas_alunos.get(codigoTurma);

			query = em.createQuery("from Disciplina d where d.codigo = :code");
			query.setParameter("code", codigoDisciplina);
			Disciplina disciplina = (Disciplina) query.getSingleResult();

			// 40 vagas por turma não são suficientes
			Turma turma = new Turma(disciplina, codigoTurma, 80, semestre);

			em.persist(turma);

			if (matriculas != null) {
				for (String matricula : matriculas) {
					query = em
							.createQuery("from Aluno a where a.matricula = :matricula");
					query.setParameter("matricula", matricula);
					Aluno aluno = (Aluno) query.getSingleResult();
					turma.inscreverAluno(aluno);
				}
				qtdInscricoes += turma.getQtdInscritos();
			}

		}

		em.getTransaction().commit();

		System.out.println("Foram importados " + alunos_nomes.keySet().size()
				+ " alunos.");

		System.out.println("Foram importadas " + disciplinas.keySet().size()
				+ " disciplinas.");

		System.out.println("Foram importadas " + turmas.keySet().size()
				+ " turmas.");

		System.out
				.println("Foram importadas " + qtdInscricoes + " inscrições.");

	}
	
}
