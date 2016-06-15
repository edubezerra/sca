package br.cefetrj.sca.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.infra.cargadados.ImportadorHistoricosEscolares;
import br.cefetrj.sca.infra.cargadados.ImportadorTurmasComInscricoes;
import jxl.read.biff.BiffException;

@Component
public class ImportacaoDadosService {

	@Autowired
	ImportadorHistoricosEscolares importadorHistoricosEscolares;

	@Autowired
	ImportadorTurmasComInscricoes importadorTurmasComInscricoes;

	public String importar(MultipartFile file, Long tipoImportacao) {
		switch (tipoImportacao.intValue()) {
		case 9:
			return this.importarHistoricoEscolar(file);
		case 10:
			return this.importarTurmasComInscricoes(file);
		}
		return null;
	}

	public String importarHistoricoEscolar(MultipartFile file) {
		String response = null;
		try {
			File tempFile = File.createTempFile("import-historico", "");
			file.transferTo(tempFile);

			response = importadorHistoricosEscolares.importarPlanilha(tempFile);

		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return "Erro ao importar a planilha do histórico escolar.";
		}

		return response;
	}

	private String importarTurmasComInscricoes(MultipartFile file) {
		String response = null;
		try {
			File tempFile = File.createTempFile("import-historico", "");
			file.transferTo(tempFile);

			response = importadorTurmasComInscricoes.importarPlanilha(tempFile);

		} catch (IOException | BiffException e) {
			e.printStackTrace();
			return "Erro ao importar a planilha do histórico escolar.";
		}

		return response;
	}
}
