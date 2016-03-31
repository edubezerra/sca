package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.VersaoCurso;

public interface CursoRepositorio extends JpaRepository<Curso, Serializable> {

	Curso findCursoBySigla(String codigoSigla);

	@Query("FROM VersaoCurso v WHERE v.curso.sigla = ?1 and v.numero = ?2")
	VersaoCurso getVersaoCurso(String codigoSigla, String numeroVersao);
}
