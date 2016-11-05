package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.DocumentoProfessor;

public interface DocumentoProfessorRepositorio extends JpaRepository<DocumentoProfessor, Serializable> {

	DocumentoProfessor findDocumentoProfessorById(Long id);

	@Query("SELECT dp from DocumentoProfessor dp WHERE dp.professor.matricula = ?1")
	DocumentoProfessor findDocumentoProfessorByMatricula(String matricula);

	@Query("from DocumentoProfessor dp where dp.professor.matricula = ?1 order by dp.nome asc")
	List<DocumentoProfessor> findAllByMatricula(String matricula);

}