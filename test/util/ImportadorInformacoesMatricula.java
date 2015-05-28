package util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
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
 * Essa classe faz a carga de dados de uma planilha para o banco de dados. Os
 * dados importados são sobre (1) disciplinas, (2) turmas, (3) alunos e (4)
 * inscrições de alunos em turmas.
 * 
 * @author Eduardo Bezerra
 *
 */
public class ImportadorInformacoesMatricula {

	/**
	 * Dicionário de pares (matrícula, nome) de cada aluno.
	 */
	private HashMap<String, String> alunos;

	/**
	 * Dicionário de pares (código, nome) de cada aluno.
	 */
	private HashMap<String, String> disciplinas;

	// código, semestre letivo { ano, período }
	private HashMap<String, SemestreLetivo> turmas;

	/**
	 * Dicionário de pares (código da turma, código da disciplina).
	 */
	private HashMap<String, String> turmas_disciplinas;

	/**
	 * Dicionário de pares (código da turma, {matrícula de aluno}).
	 */
	private HashMap<String, Set<String>> turmas_alunos;

	public ImportadorInformacoesMatricula() {
		alunos = new HashMap<>();
		disciplinas = new HashMap<>();
		turmas = new HashMap<>();
		turmas_disciplinas = new HashMap<>();
		turmas_alunos = new HashMap<>();
	}

	public static void main(String[] args) {
		ImportadorInformacoesMatricula iim = new ImportadorInformacoesMatricula();
		iim.importarPlanilha("./grades-curriculares/Matrícula-DEPIN-2015-1.xls");
		iim.gravarDadosImportados("SCAPU");
	}

	public void importarPlanilha(String inputFile) {
		File inputWorkbook = new File(inputFile);
		Workbook w;

		try {
			WorkbookSettings ws = new WorkbookSettings();
			ws.setEncoding("Cp1252");
			w = Workbook.getWorkbook(inputWorkbook, ws);
			Sheet sheet = w.getSheet(0);

			for (int i = 1; i < sheet.getRows(); i++) {

				String aluno_matricula = sheet.getCell(13, i).getContents();
				String aluno_nome = sheet.getCell(1, i).getContents();
				alunos.put(aluno_matricula, aluno_nome);

				String disciplina_codigo = sheet.getCell(4, i).getContents();
				String disciplina_nome = sheet.getCell(5, i).getContents();
				disciplinas.put(disciplina_codigo, disciplina_nome);

				String turma_codigo = sheet.getCell(11, i).getContents();
				String semestre_ano = sheet.getCell(15, i).getContents();
				String semestre_periodo = sheet.getCell(16, i).getContents();

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

				String situacao = sheet.getCell(10, i).getContents();

				if (situacao.equals("Aceita/Matriculada")) {
					if (!turmas_alunos.containsKey(turma_codigo)) {
						turmas_alunos.put(turma_codigo, new HashSet<String>());
					}

					turmas_alunos.get(turma_codigo).add(aluno_matricula);
				}
			}
		} catch (BiffException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void gravarDadosImportados(String managerName) {
		EntityManagerFactory emf = Persistence
				.createEntityManagerFactory(managerName);
		EntityManager em = emf.createEntityManager();

		em.getTransaction().begin();

		try {
			Set<String> alunosIt = alunos.keySet();

			for (String matricula : alunosIt) {
				em.persist(new Aluno(alunos.get(matricula), matricula));
			}
			Set<String> disciplinasIt = disciplinas.keySet();

			for (String codigoDisciplina : disciplinasIt) {
				em.persist(new Disciplina(disciplinas.get(codigoDisciplina),
						codigoDisciplina, 4));
			}

			Set<String> turmasIt = turmas.keySet();
			Query query;

			int qtdInscricoes = 0;

			for (String codigoTurma : turmasIt) {
				SemestreLetivo semestre = turmas.get(codigoTurma);
				String codigoDisciplina = turmas_disciplinas.get(codigoTurma);
				Set<String> matriculas = turmas_alunos.get(codigoTurma);

				query = em
						.createQuery("from Disciplina d where d.codigo = :code");
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

			System.out.println("Foram importados " + alunos.keySet().size()
					+ " alunos.");

			System.out.println("Foram importadas "
					+ disciplinas.keySet().size() + " disciplinas.");

			System.out.println("Foram importadas " + turmas.keySet().size()
					+ " turmas.");

			System.out.println("Foram importadas " + qtdInscricoes
					+ " inscrições.");

		} catch (IllegalArgumentException | IllegalStateException ex) {
			System.err.println(ex.getMessage());
			System.exit(1);
		} finally {
			em.close();
			emf.close();
		}
	}
}
