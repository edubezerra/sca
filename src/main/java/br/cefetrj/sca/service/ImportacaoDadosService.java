package br.cefetrj.sca.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.infra.cargadados.ImportadorAlocacoesProfessoresEmTurmas;
import br.cefetrj.sca.infra.cargadados.ImportadorAtividadesComplementares;
import br.cefetrj.sca.infra.cargadados.ImportadorGradesCurriculares;
import br.cefetrj.sca.infra.cargadados.ImportadorHistoricosEscolares;
import br.cefetrj.sca.infra.cargadados.ImportadorPesquisaAvaliacaoProfessor;
import br.cefetrj.sca.infra.cargadados.ImportadorProfessores;
import br.cefetrj.sca.infra.cargadados.ImportadorTurmasComInscricoes;
import br.cefetrj.sca.infra.cargadados.ImportadorUsuariosAlunosUsandoMatriculaComoLogin;
import jxl.read.biff.BiffException;

/**
 * 
 * @author Eduardo Bezerra
 * 
 * @see <code>DescritorImportacaoDados</code>
 *
 */
@Service
public class ImportacaoDadosService {

	@Autowired
	ImportadorGradesCurriculares importadorGradesCurriculares;

	@Autowired
	ImportadorHistoricosEscolares importadorHistoricosEscolares;

	@Autowired
	ImportadorTurmasComInscricoes importadorTurmasComInscricoes;

	@Autowired
	ImportadorPesquisaAvaliacaoProfessor importadorPesquisaAvaliacaoProfessor;

	@Autowired
	ImportadorUsuariosAlunosUsandoMatriculaComoLogin importadorUsuariosAlunos;

	@Autowired
	ImportadorAtividadesComplementares importadorAC;

	@Autowired
	ImportadorProfessores importadorProfessores;

	@Autowired
	ImportadorAlocacoesProfessoresEmTurmas importadorAlocacoesProfessoresEmTurmas;

	@Transactional
	public String importar(MultipartFile file, Long tipoImportacao) {
		if (tipoImportacao == null) {
			throw new IllegalArgumentException("Tipo de importação deve ser fornecido.");
		}
		try {
			File tempFile = criarArquivoTemporario(file);

			switch (tipoImportacao.intValue()) {
			case 1:
				return importadorGradesCurriculares.importarPlanilha(tempFile);
			case 2:
				return importadorHistoricosEscolares.importarPlanilha(tempFile);
			case 3:
				return importadorTurmasComInscricoes.importarPlanilha(tempFile);
			case 4:
				return importadorPesquisaAvaliacaoProfessor.run();
			case 5:
				return importadorUsuariosAlunos.run();
			case 6:
				return importadorAC.importarPlanilha(tempFile);
			case 7:
				return importadorProfessores.importarPlanilha(tempFile);
			}
		} catch (java.lang.IllegalArgumentException e) {
			return e.getMessage();
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return "Erro ao importar a planilha.";
		}
		return "Importador não disponível!";
	}

	private File criarArquivoTemporario(MultipartFile file) {
		if (file == null) {
			throw new IllegalArgumentException("Arquivo deve ser fornecido.");
		}
		File tempFile;
		try {
			tempFile = File.createTempFile("import-planilha", "");
			file.transferTo(tempFile);
			return tempFile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Transactional
	public String importarHistoricosEscolares(MultipartFile file) {
		try {
			File tempFile = criarArquivoTemporario(file);
			return importadorHistoricosEscolares.importarPlanilha(tempFile);
		} catch (java.lang.IllegalArgumentException e) {
			return e.getMessage();
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return "Erro ao importar a planilha de alunos e históricos escolares.";
		}

	}

	@Transactional
	public String importarGradesCurriculares(MultipartFile file) {
		try {
			File tempFile = criarArquivoTemporario(file);
			return importadorGradesCurriculares.importarPlanilha(tempFile);
		} catch (java.lang.IllegalArgumentException e) {
			return e.getMessage();
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return "Erro ao importar a planilha de grades curriculares.";
		}

	}

	public String importarAlocacoesProfessoresEmTurmas(MultipartFile file) {
		try {
			File tempFile = criarArquivoTemporario(file);
			return importadorAlocacoesProfessoresEmTurmas.importarPlanilha(tempFile);
		} catch (java.lang.IllegalArgumentException e) {
			return e.getMessage();
		}

	}
}
