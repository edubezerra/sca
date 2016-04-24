package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.Professor;

public interface ProfessorRepositorio extends
		JpaRepository<Professor, Serializable> {

	Professor findProfessorByMatricula(String matricula);
}
