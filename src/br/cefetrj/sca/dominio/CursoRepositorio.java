package br.cefetrj.sca.dominio;

public interface CursoRepositorio {
	Curso getCursoPorSigla(String codigoSigla);
	VersaoCurso getVersaoCurso(String codigoSigla, String numeroVersao);
}
