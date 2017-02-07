package br.cefetrj.sca.infra.cargadados;

import java.io.File;

public interface ImportadorDados {
	String importarPlanilha(File inputWorkbook);
}
