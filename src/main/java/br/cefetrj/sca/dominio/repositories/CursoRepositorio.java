package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.VersaoCurso;

public interface CursoRepositorio extends JpaRepository<Curso, Serializable> {

	Curso findCursoBySigla(String codigoSigla);
	
	@Query("FROM Curso c WHERE c.coordenadorAtividadesComplementares.matricula = ?1")
	List<Curso> findAllCursoByCoordenadorAtividades(String matriculaCoordenador);
	
	@Query("FROM VersaoCurso v WHERE v.curso.coordenadorAtividadesComplementares.matricula = ?1")
	List<VersaoCurso> findAllVersaoCursoByCoordenadorAtividades(String matriculaCoordenador);

	@Query("FROM VersaoCurso v WHERE v.curso.sigla = ?1 and v.numero = ?2")
	VersaoCurso getVersaoCurso(String codigoSigla, String numeroVersao);
	
	@Query("FROM VersaoCurso v WHERE v.curso.sigla = ?1")
	List<VersaoCurso> findAllVersaoCursoByCurso(String codigoSigla);
	
	@Query("FROM VersaoCurso v")
	List<VersaoCurso> findAllVersaoCurso();
}
