package br.cefetrj.sca.infra.cargadados;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.persistence.Query;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.atividadecomplementar.AtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.EnumTipoAtividadeComplementar;
import br.cefetrj.sca.dominio.atividadecomplementar.TabelaAtividadesComplementares;
import br.cefetrj.sca.dominio.atividadecomplementar.TipoAtividadeComplementar;
import br.cefetrj.sca.dominio.repositories.TipoAtividadeComplementarRepositorio;

@Component
public class ImportadorAtividadesComp {

	@Autowired
	TipoAtividadeComplementarRepositorio tipoAtividadeComplementarRepositorio;
	
	@Autowired
	TabelaAtividadesComplementares

	// EntityManager em = ImportadorTudo.entityManager;

	static String colunas[] = { "DESCRICAO_ATIVIDADE", "CATEGORIA_ATIVIDADE",
			"CH_MIN", "CH_MAX", "COD_ESTRUTURADO", "NOME_UNIDADE",
			"SIGLA_UNIDADE", "COD_CURSO", "NUM_VERSAO", "ID_VERSAO_CURSO",
			"CH_MIN_ATIVIDADES" };

	private HashMap<String, VersaoCurso> versoesCursos = new HashMap<>();

	private List<TipoAtividadeComplementar> tiposAtividades = new ArrayList<>();

	private HashMap<String, TabelaAtividadesComplementares> tabAtividades = new HashMap<>();

	public void run() {
		System.out.println("ImportadorAtividadesComp.run()");
		try {
			ImportadorAtividadesComp iim = new ImportadorAtividadesComp();
			String arquivoPlanilha = "./planilhas/grades-curriculares/AtividadesCompBCC.xls";
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();

			iim = new ImportadorAtividadesComp();
			arquivoPlanilha = "./planilhas/grades-curriculares/AtividadesCompCSTSI.xls";
			iim.importarPlanilha(arquivoPlanilha);
			iim.gravarDadosImportados();
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Feito!");
	}

	private void gravarDadosImportados() {
		// em.getTransaction().begin();

		/**
		 * Realiza a persistência dos objetos
		 * <code>TipoAtividadeComplementar</code>.
		 */
		for (TipoAtividadeComplementar tipoAtiv : tiposAtividades) {
			System.out.println("Gravando tipo de atividade complementar: "
					+ tipoAtiv);
			tipoAtividadeComplementarRepositorio.save(tipoAtiv);
//			em.persist(tipoAtiv);
		}

		/**
		 * Realiza a persistência dos objetos <code>VersaoCurso</code> e
		 * <code>TabelaAtividadesComplementares</code>.
		 */
		Set<String> versoesIt = versoesCursos.keySet();
		for (String numeroMaisSigla : versoesIt) {
			VersaoCurso versaoCurso = versoesCursos.get(numeroMaisSigla);
			System.out.println("Gravando atividades em " + versaoCurso);
			TabelaAtividadesComplementares tab = tabAtividades
					.get(numeroMaisSigla);
			em.persist(tab);

			versaoCurso.setTabelaAtividades(tab);
			em.merge(versaoCurso);
		}

		// em.getTransaction().commit();

		for (String numeroMaisSigla : versoesIt) {
			VersaoCurso versaoCurso = versoesCursos.get(numeroMaisSigla);
			System.out.println("Foram importadas "
					+ tabAtividades.get(numeroMaisSigla).getQtdAtividades()
					+ " atividades complementares em " + versaoCurso);
		}
	}

	public void importarPlanilha(String inputFile) throws BiffException,
			IOException {
		File inputWorkbook = new File(inputFile);
		importarPlanilha(inputWorkbook);
	}

	private void importarPlanilha(File arquivoPlanilha) throws BiffException,
			IOException {
		Workbook w;

		System.err.println("Abrindo planilha " + arquivoPlanilha);

		List<String> colunasList = Arrays.asList(colunas);
		WorkbookSettings ws = new WorkbookSettings();
		ws.setEncoding("Cp1252");
		w = Workbook.getWorkbook(arquivoPlanilha, ws);
		Sheet sheet = w.getSheet(0);

		importarVersoesCursos(colunasList, sheet);
		importarTiposAtividadesComp(colunasList, sheet);
		importarAtividadesComp(colunasList, sheet);
	}

	private VersaoCurso getVersaoCurso(String siglaCurso, String numeroVersao) {
		Query query = em
				.createQuery("from VersaoCurso versao "
						+ "where versao.numero = :numeroVersao and versao.curso.sigla = :siglaCurso");
		query.setParameter("numeroVersao", numeroVersao);
		query.setParameter("siglaCurso", siglaCurso);
		return (VersaoCurso) query.getSingleResult();
	}

	private void importarVersoesCursos(List<String> colunasList, Sheet sheet) {
		System.out
				.println("Iniciando importação de dados relativos a versões de cursos...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String codCurso = sheet
					.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"),
					i).getContents();
			String chMinAtividades = sheet.getCell(
					colunasList.indexOf("CH_MIN_ATIVIDADES"), i).getContents();
			if (versoesCursos.get(codCurso + numVersao) == null) {
				VersaoCurso versao = getVersaoCurso(codCurso, numVersao);

				Duration chAtiv = Duration.ofHours(Long
						.parseLong(chMinAtividades));
				versao.setCargaHorariaMinAitvComp(chAtiv);

				versoesCursos.put(codCurso + numVersao, versao);
			}

		}
		System.out.println("Foram encontradas " + versoesCursos.size()
				+ " versões de cursos.");
	}

	private void importarTiposAtividadesComp(List<String> colunasList,
			Sheet sheet) {
		System.out
				.println("Iniciando importação de dados relativos a tipos de atividade complementar...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String descrAtividade = sheet.getCell(
					colunasList.indexOf("DESCRICAO_ATIVIDADE"), i)
					.getContents();
			String categoria = sheet.getCell(
					colunasList.indexOf("CATEGORIA_ATIVIDADE"), i)
					.getContents();

			boolean tipoAtividadeJaExiste = false;
			for (TipoAtividadeComplementar tipoAtiv : tiposAtividades) {
				if (tipoAtiv.getDescricao().equals(descrAtividade)
						&& tipoAtiv.getCategoria().equals(categoria)) {
					tipoAtividadeJaExiste = true;
					break;
				}
			}

			if (!tipoAtividadeJaExiste) {
				TipoAtividadeComplementar tipoAtiv = new TipoAtividadeComplementar(
						descrAtividade,
						EnumTipoAtividadeComplementar.findByText(categoria));

				tiposAtividades.add(tipoAtiv);
			} else {
				System.err
						.println("Já existe tipo de atividade complementar com esta descricao "
								+ descrAtividade);
			}
		}
	}

	private TipoAtividadeComplementar getTipoAtividadeComp(
			String descrAtividade, String categoria) {
		for (TipoAtividadeComplementar tipoAtiv : tiposAtividades) {
			if (tipoAtiv.getDescricao().equals(descrAtividade)
					&& tipoAtiv.getCategoria()
							.equals(EnumTipoAtividadeComplementar
									.findByText(categoria))) {
				return tipoAtiv;
			}
		}
		return null;
	}

	private void importarAtividadesComp(List<String> colunasList, Sheet sheet) {
		System.out
				.println("Iniciando importação de dados relativos à tabela de atividades complementares...");
		for (int i = 1; i < sheet.getRows(); i++) {
			String descrAtividade = sheet.getCell(
					colunasList.indexOf("DESCRICAO_ATIVIDADE"), i)
					.getContents();
			String categoria = sheet.getCell(
					colunasList.indexOf("CATEGORIA_ATIVIDADE"), i)
					.getContents();
			String cargaHorMin = sheet
					.getCell(colunasList.indexOf("CH_MIN"), i).getContents();
			String cargaHorMax = sheet
					.getCell(colunasList.indexOf("CH_MAX"), i).getContents();
			String codCurso = sheet
					.getCell(colunasList.indexOf("COD_CURSO"), i).getContents();
			String numVersao = sheet.getCell(colunasList.indexOf("NUM_VERSAO"),
					i).getContents();

			String numeroMaisSigla = codCurso + numVersao;
			if (tabAtividades.get(numeroMaisSigla) == null) {
				TabelaAtividadesComplementares tab = new TabelaAtividadesComplementares();

				Duration chMax = Duration.ofHours(Long.parseLong(cargaHorMax));
				Duration chMin = Duration.ofHours(Long.parseLong(cargaHorMin));
				AtividadeComplementar ativ = new AtividadeComplementar(
						getTipoAtividadeComp(descrAtividade, categoria), chMax,
						chMin);

				tab.adicionarAtividade(ativ);
				tabAtividades.put(numeroMaisSigla, tab);
			} else {
				boolean atividadeJaExiste = false;
				for (AtividadeComplementar ativ : tabAtividades.get(
						numeroMaisSigla).getAtividades()) {
					if (ativ.getTipo().getDescricao().equals(descrAtividade)
							&& ativ.getTipo().getCategoria().equals(categoria)) {
						atividadeJaExiste = true;
						break;
					}
				}
				if (!atividadeJaExiste) {
					Duration chMax = Duration.ofHours(Long
							.parseLong(cargaHorMax));
					Duration chMin = Duration.ofHours(Long
							.parseLong(cargaHorMin));
					AtividadeComplementar ativ = new AtividadeComplementar(
							getTipoAtividadeComp(descrAtividade, categoria),
							chMax, chMin);

					tabAtividades.get(numeroMaisSigla).adicionarAtividade(ativ);
				} else {
					System.err
							.println("Já existe atividade complementar com esta descricao "
									+ descrAtividade);
				}
			}
		}
	}
}
