package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.atividadecomplementar.AtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.EnumTipoAtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.TabelaAtividadesComplementares;
import br.cefetrj.sca.dominio.atividadecomplementar.TipoAtividadeComplementar;
import br.cefetrj.sca.dominio.repositories.TipoAtividadeComplementarRepositorio;
import br.cefetrj.sca.dominio.repositories.VersaoCursoRepositorio;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

@Component
public class ImportadorAtividadesComplementares {

	@Autowired
	TipoAtividadeComplementarRepositorio tipoAtividadeComplementarRepositorio;

	@Autowired
	VersaoCursoRepositorio versaoCursoRepositorio;

	static String colunas[] = { "DESCRICAO_ATIVIDADE", "CATEGORIA_ATIVIDADE", "CH_MIN", "CH_MAX", "COD_ESTRUTURADO",
			"NOME_UNIDADE", "SIGLA_UNIDADE", "COD_CURSO", "NUM_VERSAO", "ID_VERSAO_CURSO", "CH_MIN_ATIVIDADES" };

	private HashMap<String, VersaoCurso> versoesCursos = new HashMap<>();

	private List<TipoAtividadeComplementar> tiposAtividades = new ArrayList<>();

	private HashMap<String, TabelaAtividadesComplementares> tabAtividades = new HashMap<>();

	private StringBuilder gravarDadosImportados(StringBuilder response) {

		/**
		 * Realiza a persistência dos objetos
		 * <code>TipoAtividadeComplementar</code>.
		 */
		for (TipoAtividadeComplementar tipoAtiv : tiposAtividades) {
			response.append("Gravando tipo de atividade complementar: " + tipoAtiv);
			tipoAtividadeComplementarRepositorio.save(tipoAtiv);
		}

		/**
		 * Realiza a persistência dos objetos <code>VersaoCurso</code> e
		 * <code>TabelaAtividadesComplementares</code>.
		 */
		Set<String> versoesIt = versoesCursos.keySet();
		for (String numeroMaisSigla : versoesIt) {
			VersaoCurso versaoCurso = versoesCursos.get(numeroMaisSigla);
			VersaoCurso versaoExistente = versaoCursoRepositorio.findByNumeroEmCurso(versaoCurso.getNumero(),
					versaoCurso.getCurso().getSigla());
			if (versaoExistente != null) {
				versaoCurso = versaoExistente;
				if (versaoCurso.getTabelaAtividades() != null) {

				}
			}
			response.append("Gravando atividades em " + versaoCurso + ";");
			TabelaAtividadesComplementares tab = tabAtividades.get(numeroMaisSigla);
			versaoCurso.setTabelaAtividades(tab);
			versaoCursoRepositorio.save(versaoCurso);
		}

		for (String numeroMaisSigla : versoesIt) {
			VersaoCurso versaoCurso = versoesCursos.get(numeroMaisSigla);
			response.append("Foram importadas " + tabAtividades.get(numeroMaisSigla).getQtdAtividades()
					+ " atividades complementares em " + versaoCurso + ";");
		}

		return response;
	}

	public String importarPlanilha(File arquivoPlanilha) throws BiffException, IOException {
		StringBuilder response = new StringBuilder();

		Workbook w;

		System.err.println("Abrindo planilha " + arquivoPlanilha);

		List<String> colunasList = Arrays.asList(colunas);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(arquivoPlanilha, ws);
		Sheet sheet = w.getSheet(0);

		importarVersoesCursos(colunasList, sheet, response);
		importarTiposAtividadesComp(colunasList, sheet, response);
		importarAtividadesComp(colunasList, sheet, response);

		response = gravarDadosImportados(response);

		return response.toString();
	}

	private VersaoCurso getVersaoCurso(String siglaCurso, String numeroVersao) {
		return versaoCursoRepositorio.findByNumeroEmCurso(numeroVersao, siglaCurso);
	}

	private void importarVersoesCursos(List<String> colunasList, Sheet sheet, StringBuilder response) {
		response.append("Iniciando importação de dados relativos a versões de cursos...;");
		for (int i = 1; i < sheet.getRows(); i++) {
			String codCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();
			String chMinAtividades = sheet.getCell(colunasList.indexOf("CH_MIN_ATIVIDADES"), i).getContents();
			if (versoesCursos.get(codCurso + numVersao) == null) {
				VersaoCurso versao = getVersaoCurso(codCurso, numVersao);

				if (versao != null) {
					Duration chAtiv = Duration.ofHours(Long.parseLong(chMinAtividades));
					versao.setCargaHorariaMinAitvComp(chAtiv);
					versoesCursos.put(codCurso + numVersao, versao);
				} else {
					throw new IllegalArgumentException("Versão de curso não encontrada: " + codCurso + "/" + numVersao);
				}
			}
		}
		response.append("Foram encontradas " + versoesCursos.size() + " versões de cursos.;");
	}

	private void importarTiposAtividadesComp(List<String> colunasList, Sheet sheet, StringBuilder response) {
		response.append("Iniciando importação de dados relativos a tipos de atividade complementar...;");
		for (int i = 1; i < sheet.getRows(); i++) {
			String descrAtividade = sheet.getCell(colunasList.indexOf("DESCRICAO_ATIVIDADE"), i).getContents();
			String categoria = sheet.getCell(colunasList.indexOf("CATEGORIA_ATIVIDADE"), i).getContents();

			boolean tipoAtividadeJaExiste = false;
			for (TipoAtividadeComplementar tipoAtiv : tiposAtividades) {
				if (tipoAtiv.getDescricao().equals(descrAtividade) && tipoAtiv.getCategoria().equals(categoria)) {
					tipoAtividadeJaExiste = true;
					break;
				}
			}

			if (!tipoAtividadeJaExiste) {
				TipoAtividadeComplementar tipoAtivExistente = tipoAtividadeComplementarRepositorio
						.findByDescricaoAndCategoria(descrAtividade,
								EnumTipoAtividadeComplementar.findByText(categoria));

				TipoAtividadeComplementar tipoAtiv;
				if (tipoAtivExistente != null) {
					tipoAtiv = tipoAtivExistente;
				} else {
					tipoAtiv = new TipoAtividadeComplementar(descrAtividade,
							EnumTipoAtividadeComplementar.findByText(categoria));
				}

				tiposAtividades.add(tipoAtiv);
			} else {
				response.append("Já existe tipo de atividade complementar com esta descricão " + descrAtividade + ";");
			}
		}

	}

	private TipoAtividadeComplementar getTipoAtividadeComp(String descrAtividade, String categoria) {
		for (TipoAtividadeComplementar tipoAtiv : tiposAtividades) {
			if (tipoAtiv.getDescricao().equals(descrAtividade)
					&& tipoAtiv.getCategoria().equals(EnumTipoAtividadeComplementar.findByText(categoria))) {
				return tipoAtiv;
			}
		}
		return null;
	}

	private void importarAtividadesComp(List<String> colunasList, Sheet sheet, StringBuilder response) {
		response.append("Iniciando importação de dados relativos à tabela de atividades complementares...;");
		for (int i = 1; i < sheet.getRows(); i++) {
			String descrAtividade = sheet.getCell(colunasList.indexOf("DESCRICAO_ATIVIDADE"), i).getContents();
			String categoria = sheet.getCell(colunasList.indexOf("CATEGORIA_ATIVIDADE"), i).getContents();
			String cargaHorMin = sheet.getCell(colunasList.indexOf("CH_MIN"), i).getContents();
			String cargaHorMax = sheet.getCell(colunasList.indexOf("CH_MAX"), i).getContents();
			String codCurso = sheet.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"), i).getContents();

			String numeroMaisSigla = codCurso + numVersao;
			if (tabAtividades.get(numeroMaisSigla) == null) {
				TabelaAtividadesComplementares tab = new TabelaAtividadesComplementares();

				Duration chMax = Duration.ofHours(Long.parseLong(cargaHorMax));
				Duration chMin = Duration.ofHours(Long.parseLong(cargaHorMin));
				AtividadeComplementar ativ = new AtividadeComplementar(getTipoAtividadeComp(descrAtividade, categoria),
						chMax, chMin);

				tab.adicionarAtividade(ativ);
				tabAtividades.put(numeroMaisSigla, tab);
			} else {
				boolean atividadeJaExiste = false;
				for (AtividadeComplementar ativ : tabAtividades.get(numeroMaisSigla).getAtividades()) {
					if (ativ.getTipo().getDescricao().equals(descrAtividade)
							&& ativ.getTipo().getCategoria().equals(categoria)) {
						atividadeJaExiste = true;
						break;
					}
				}
				if (!atividadeJaExiste) {
					Duration chMax = Duration.ofHours(Long.parseLong(cargaHorMax));
					Duration chMin = Duration.ofHours(Long.parseLong(cargaHorMin));
					AtividadeComplementar ativ = new AtividadeComplementar(
							getTipoAtividadeComp(descrAtividade, categoria), chMax, chMin);

					tabAtividades.get(numeroMaisSigla).adicionarAtividade(ativ);
				} else {
					response.append("Já existe atividade complementar com esta descricao " + descrAtividade + ";");
				}
			}
		}
	}
}
