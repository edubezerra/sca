package br.cefetrj.sca.service;

import java.io.File;
import java.io.IOException;

import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.infra.cargadados.ImportadorAtividadesComplementares;
import br.cefetrj.sca.infra.cargadados.ImportadorGradesCurriculares;
import br.cefetrj.sca.infra.cargadados.ImportadorHistoricosEscolares;
import br.cefetrj.sca.infra.cargadados.ImportadorPesquisaAvaliacaoProfessor;
import br.cefetrj.sca.infra.cargadados.ImportadorTurmasComInscricoes;
import br.cefetrj.sca.infra.cargadados.ImportadorUsuariosAlunosUsandoCpfComoLogin;

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
	ImportadorUsuariosAlunosUsandoCpfComoLogin importadorUsuariosAlunos;

	@Autowired
	ImportadorAtividadesComplementares importadorAC;

	@Transactional
	public String importar(MultipartFile file, Long tipoImportacao) {
		try {
			File tempFile = File.createTempFile("import-planilha",
					"");
			file.transferTo(tempFile);

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
			}
		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return "Erro ao importar a planilha.";
		}
		return "Importador não disponível!";
	}
}
