package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.VersaoCurso;

@Component
public interface VersaoCursoRepositorio extends
		JpaRepository<VersaoCurso, Serializable> {
	@Query("from VersaoCurso versao where versao.numero = ?1 and versao.curso.sigla = ?2")
	public VersaoCurso findByNumeroEmCurso(String numeroVersao,
			String siglaCurso);

	@Query("from VersaoCurso versao where versao.numero = ?1 and versao.curso = ?2")
	public VersaoCurso findByNumeroEmCurso(String numero, Curso curso);
}
