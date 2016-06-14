package br.cefetrj.sca.service;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import br.cefetrj.sca.infra.cargadados.ImportadorHistoricosEscolares;
import jxl.read.biff.BiffException;

@Component
public class ImportacaoHistoricosEscolaresService {

	@Autowired
	ImportadorHistoricosEscolares importadorHistoricosEscolares;

	public String importaHistoricoEscolar(MultipartFile file) {
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
}
