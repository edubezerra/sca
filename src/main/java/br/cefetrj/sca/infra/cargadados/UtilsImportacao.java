package br.cefetrj.sca.infra.cargadados;

import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;

public class UtilsImportacao {
	public static PeriodoLetivo getPeriodoLetivo(String semestre_ano,
			String semestre_periodo) {
		int ano = Integer.parseInt(semestre_ano);
		PeriodoLetivo.EnumPeriodo periodo = null;

		if (semestre_periodo.equals("1º Semestre")) {
			periodo = EnumPeriodo.PRIMEIRO;
		} else if (semestre_periodo.equals("2º Semestre")) {
			periodo = EnumPeriodo.SEGUNDO;
		}
		return new PeriodoLetivo(ano, periodo);
	}

}
