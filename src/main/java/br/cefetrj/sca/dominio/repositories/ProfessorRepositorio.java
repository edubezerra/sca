package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;

public interface ProfessorRepositorio extends
		JpaRepository<Professor, Serializable> {

	Professor findProfessorByMatricula(String matricula);

	@Query("SELECT p from Departamento d JOIN d.professores p WHERE d = ?1")
	Professor findProfessorByDepartamento(Departamento departamento);

}
