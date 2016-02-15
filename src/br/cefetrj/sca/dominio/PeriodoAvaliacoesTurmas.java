package br.cefetrj.sca.dominio;

import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;

/**
 * Representa o período (intervalo de tempo) em que a avaliação de turmas está
 * disponíveis para os alunos.
 * 
 * @author Eduardo Bezerra
 *
 */
public class PeriodoAvaliacoesTurmas {

	private static PeriodoAvaliacoesTurmas INSTANCE;

	static {
		try {
			JSONParser parser = new JSONParser();

			URL url = Thread.currentThread().getContextClassLoader()
					.getResource("periodo-avaliacoes-turmas.txt");
			File file = new File(url.getPath());

			Object obj = parser.parse(new InputStreamReader(FileUtils
					.openInputStream(file)));

			JSONObject jsonObject = (JSONObject) obj;

			String ano = (String) jsonObject.get("Ano");
			String periodo = (String) jsonObject.get("Periodo");
			String dataInicio = (String) jsonObject.get("DataInicio");
			String dataTermino = (String) jsonObject.get("DataTermino");

			PeriodoLetivo semestreLetivoReferencia = null;
			if (periodo.equals("1")) {
				semestreLetivoReferencia = new PeriodoLetivo(
						Integer.parseInt(ano), EnumPeriodo.PRIMEIRO);
			} else if (periodo.equals("2")) {
				semestreLetivoReferencia = new PeriodoLetivo(
						Integer.parseInt(ano), EnumPeriodo.SEGUNDO);
			}

			DateFormat formato = new SimpleDateFormat("dd/mm/yyyy");

			INSTANCE = new PeriodoAvaliacoesTurmas(semestreLetivoReferencia,
					formato.parse(dataInicio), formato.parse(dataTermino));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Prazo para avaliações não pode ser obtido.");
		}
	}

	/**
	 * Início da avaliação.
	 */
	Date dataInicio;

	/**
	 * Término da avaliação.
	 */
	Date dataTermino;

	/**
	 * Avaliação referente às turmas deste semestre letivo.
	 */
	PeriodoLetivo semestreLetivo;

	Boolean interrompido;

	public PeriodoAvaliacoesTurmas(PeriodoLetivo semestreLetivo,
			Date dataInicio, Date dataTermino) {
		this.semestreLetivo = semestreLetivo;
		this.dataInicio = dataInicio;
		this.dataTermino = dataTermino;
		this.interrompido = null;
	}

	public void iniciar() {
		if (dataInicio == null) {
			this.dataInicio = new Date();
		} else {
			throw new IllegalStateException(
					"Período de avaliações já iniciado.");
		}
	}

	public void finalizar() {
		if (dataTermino == null) {
			this.dataTermino = new Date();
		} else {
			throw new IllegalStateException(
					"Período de avaliações já iniciado.");
		}
	}

	public boolean isInterrompido() {
		return interrompido;
	}

	public void interromper() {
		interrompido = true;
	}

	public void reiniciar() {
		interrompido = false;
	}

	public Date getDataInicioEfetivo() {
		return dataInicio;
	}

	public Date getDataTerminoEfetivo() {
		return dataTermino;
	}

	public PeriodoLetivo getPeriodoLetivo() {
		return semestreLetivo;
	}

	public static PeriodoAvaliacoesTurmas getInstance() {
		return INSTANCE;
	}

	public static void main(String[] args) {
		System.out.println(PeriodoAvaliacoesTurmas.getInstance()
				.getPeriodoLetivo());
	}
}
