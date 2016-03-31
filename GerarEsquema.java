package br.cefetrj.sca.dominio;

import javax.persistence.Persistence;

public class GerarEsquema {

	public static void main(String[] args) {
		Persistence.generateSchema("samplePU", null);
	}

}