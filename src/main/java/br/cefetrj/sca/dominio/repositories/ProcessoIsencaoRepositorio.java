package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.isencoes.ItemIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.ProcessoIsencaoDisciplinas;

public interface ProcessoIsencaoRepositorio extends
		JpaRepository<ProcessoIsencaoDisciplinas, Serializable> {

	ProcessoIsencaoDisciplinas findById(Long id);

	@Query("SELECT i FROM ProcessoIsencaoDisciplinas pi JOIN pi.itens i WHERE pi.id = ?")
	List<ItemIsencaoDisciplina> findItemIsencaoByProcessoIsencao(
			long idProcIsencao);

	ProcessoIsencaoDisciplinas findByAluno(Aluno aluno);

	@Query("SELECT pi FROM ProcessoIsencaoDisciplinas pi WHERE pi.aluno.matricula = ?")
	ProcessoIsencaoDisciplinas findByMatriculaAluno(String matricula);

	@Query("SELECT p FROM ProcessoIsencaoDisciplinas p WHERE p.aluno.versaoCurso.curso = ?")
	List<ProcessoIsencaoDisciplinas> findByCurso(Curso curso);

	@Query("SELECT i FROM ItemIsencaoDisciplina i WHERE i.id= ?")
	ItemIsencaoDisciplina findItemIsencaoById(Long id);

	@Query("SELECT pi FROM ProcessoIsencaoDisciplinas pi JOIN pi.itens i JOIN i.disciplina d "
			+ "WHERE d IN (SELECT disc FROM Departamento depto JOIN depto.disciplinas disc WHERE depto = ?)")
	Set<ProcessoIsencaoDisciplinas> findByDepartamento(
			Departamento departamento);

}
