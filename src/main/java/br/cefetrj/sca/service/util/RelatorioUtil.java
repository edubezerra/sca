package br.cefetrj.sca.service.util;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoLetivo;

/**
 * @author Christofer
 *
 */
public final class RelatorioUtil {

	private RelatorioUtil(){}
	
	/**
	 * Recupera o periodo letivo que está no formato ano/periodo
	 * @param strPeriodoLetivo
	 * @return
	 */
	public static PeriodoLetivo getPeriodoLetivoByPeriodoString(String strPeriodoLetivo) {
		/*
		 * indice 0 indica o ano
		 * indice 1 indica o periodo
		 * */
		String[] periodoInfos = strPeriodoLetivo.split("/");
		
		return new PeriodoLetivo(Integer.parseInt(periodoInfos[0]), Integer.parseInt(periodoInfos[1]));
	}
	
	/**
	 * Recupera as iniciais pertencentes a matricula de um aluno de determinado periodo Letivo.
	 * Por exemplo, se o periodo letivo eh 2013/1, entao a matricula inicia com 131%.
	 * @param strPeriodoLetivo
	 * @return
	 */
	public static String getPeriodoMatriculaByPeriodoString(String strPeriodoLetivo) {
		/*
		 * indice 0 indica o ano
		 * indice 1 indica o periodo
		 * */
		String[] periodoInfos = strPeriodoLetivo.split("/");
		
		/*
		 * A matricula de um aluno eh formada pelos dois ultimos digitos do ano, mais o periodo.
		 * Por exemplo, a matricula 131XXXXBCC eh do ano 2013, 1 periodo
		 * */
		return periodoInfos[0].substring(periodoInfos[0].length() - 2) + periodoInfos[1];
	}
	
	public static String createJSONResponse(Map<PeriodoLetivo, List<Aluno>> alunosPorSemestre) {
		JSONArray montaJson = new JSONArray();
		
		for(PeriodoLetivo key : alunosPorSemestre.keySet()) {
			JSONObject obj = new JSONObject();
			
			try {					
				obj.put("Periodo Letivo", key);
				obj.put("Alunos", alunosPorSemestre.get(key).size());
				
				JSONArray arrayAlunos = new JSONArray();
				for(Aluno a: alunosPorSemestre.get(key)) {
					arrayAlunos.put(a.getMatricula() + " " + a.getNome());
				}
				
				obj.put("Lista de Alunos", arrayAlunos);
				
				montaJson.put(obj);
			} catch(JSONException e) {
				e.printStackTrace();
			}
			
		}
		
		return montaJson.toString();
	}
}
