package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.VersaoCurso;

public interface AlunoRepositorio extends JpaRepository<Aluno, Serializable> {
	
	Aluno findAlunoByMatricula(String matricula);

	@Query("SELECT a from Aluno a WHERE a.pessoa.cpf = ?1")
	Aluno findAlunoByCpf(String cpf);
	
	@Query("from Aluno a where a.versaoCurso = ?1")
	List<Aluno> findAllByVersaoCurso(VersaoCurso versaoCurso);	
}