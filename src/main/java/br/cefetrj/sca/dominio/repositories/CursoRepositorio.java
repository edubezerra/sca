package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.VersaoCurso;

public interface CursoRepositorio extends JpaRepository<Curso, Serializable> {

	Curso findCursoBySigla(String codigoSigla);

	@Query("FROM Curso c WHERE c.coordenadorAtividadesComplementares.matricula = ?1")
	List<Curso> findAllCursoByCoordenadorAtividades(String matriculaCoordenador);

	@Query("FROM VersaoCurso v WHERE v.curso.coordenadorAtividadesComplementares.matricula = ?1")
	List<VersaoCurso> findAllVersaoCursoByCoordenadorAtividades(String matriculaCoordenador);

	@Query("FROM VersaoCurso v WHERE v.curso.sigla = ?1 and v.numero = ?2")
	VersaoCurso getVersaoCurso(String siglaCurso, String numeroVersao);

	@Query("FROM VersaoCurso v WHERE v.curso.sigla = ?1")
	List<VersaoCurso> findAllVersaoCursoByCurso(String codigoSigla);

	@Query("FROM VersaoCurso v")
	List<VersaoCurso> findAllVersaoCurso();

	@Query("FROM Curso c WHERE c.coordenador = ?1")
	Curso findByCoordenador(Professor professor);

	@Query("select DISTINCT sum(d.cargaHoraria) from Disciplina d JOIN d.versaoCurso v JOIN v.disciplinas d WHERE v.curso.sigla = ?1 AND v.numero = ?2")
	Integer cargaHorariaTotal(String siglaCurso, String numeroVersao);

	@Query("select DISTINCT d from Disciplina d JOIN d.versaoCurso v JOIN v.disciplinas d WHERE v.curso.sigla = ?1 AND v.numero = ?2 ORDER BY d.nome")
	List<Disciplina> findDisciplinas(String siglaCurso, String numeroVersao);
}
