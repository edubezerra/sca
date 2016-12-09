package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.AlunoFabrica;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

/**
 * Esse importador realiza a persistência dos objetos <code>Aluno</code>.
 */
@Component
public class ImportadorAlunos {

	@Autowired
	private AlunoRepositorio alunoRepositorio;

	@Autowired
	private AlunoFabrica alunoFabrica;

	String colunas[] = { "COD_CURSO", "CURSO", "VERSAO_CURSO", "CPF", "MATR_ALUNO", "NOME_PESSOA", "FORMA_EVASAO",
			"COD_TURMA", "COD_DISCIPLINA", "NOME_DISCIPLINA", "ANO", "PERIODO", "SITUACAO", "CH_TOTAL", "CREDITOS",
			"MEDIA_FINAL", "NUM_FALTAS" };

	/**
	 * Dicionário de pares (matrícula, objeto da classe aluno) de cada aluno.
	 */
	private HashMap<String, Aluno> alunos_matriculas = new HashMap<>();

	public ImportadorAlunos() {
	}

	public void run() {
		String planilhaMatriculas = "./planilhas/historicos-escolares/11.02.05.99.60.xls";
		run(planilhaMatriculas);
	}

	public void run(String arquivoPlanilha) {
		System.out.println("ImportadorAlunos.run()");
		try {
			this.importarPlanilha(arquivoPlanilha);
			this.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	public void gravarDadosImportados() {
		System.out.println("Realizando a persistência de objetos Aluno...");

		int adicionados = 0;
		Set<String> matriculas = alunos_matriculas.keySet();
		for (String matricula : matriculas) {
			Aluno aluno;
			try {
				aluno = alunoRepositorio.findAlunoByMatricula(matricula);
			} catch (NoResultException e) {
				aluno = null;
			}

			if (aluno == null) {
				alunoRepositorio.save(alunos_matriculas.get(matricula));
				adicionados++;
			}
		}

		System.out.println("Foram adicionados " + adicionados + " alunos.");
	}

	public void importarPlanilha(String inputFile) throws BiffException, IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	public void importarPlanilha(File inputWorkbook) throws BiffException, IOException {
		Workbook w;

		List<String> colunasList = Arrays.asList(colunas);
		System.out.println("Realizando leitura da planilha " + inputWorkbook.getName());

		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(inputWorkbook, ws);
		Sheet sheet = w.getSheet(0);

		for (int i = 1; i < sheet.getRows(); i++) {

			String codigoCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numeroVersaoCurso = sheet.getCell(colunasList.indexOf("VERSAO_CURSO"), i).getContents();

			String aluno_matricula = sheet.getCell(colunasList.indexOf("MATR_ALUNO"), i).getContents();
			String aluno_nome = sheet.getCell(colunasList.indexOf("NOME_PESSOA"), i).getContents();
			String aluno_cpf = sheet.getCell(colunasList.indexOf("CPF"), i).getContents();

			if (aluno_cpf == null || aluno_cpf.isEmpty()) {
				System.out.println("Aluno sem CPF definido: " + aluno_nome + ", " + aluno_matricula);
			} else {
				Aluno aluno = alunoFabrica.criar(aluno_nome, aluno_matricula, aluno_cpf, codigoCurso,
						numeroVersaoCurso);
				alunos_matriculas.put(aluno_matricula, aluno);
			}

		}
		System.out.println("Dados lidos com sucesso!");
	}

	public static void main(String[] args) {
		new ImportadorAlunos().run();
	}
}
