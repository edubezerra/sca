package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Curso;
import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.isencoes.ItemPedidoIsencaoDisciplina;
import br.cefetrj.sca.dominio.isencoes.PedidoIsencaoDisciplinas;

public interface PedidoIsencaoDisciplinasRepositorio extends
		JpaRepository<PedidoIsencaoDisciplinas, Serializable> {

	PedidoIsencaoDisciplinas findById(Long id);

	@Query("SELECT i FROM PedidoIsencaoDisciplinas pi JOIN pi.itens i WHERE pi.id = ?")
	List<ItemPedidoIsencaoDisciplina> findItemIsencaoByProcessoIsencao(
			long idProcIsencao);

	PedidoIsencaoDisciplinas findByAluno(Aluno aluno);

	@Query("SELECT pi FROM PedidoIsencaoDisciplinas pi WHERE pi.aluno.matricula = ?")
	PedidoIsencaoDisciplinas findByMatriculaAluno(String matricula);

	@Query("SELECT p FROM PedidoIsencaoDisciplinas p WHERE p.aluno.versaoCurso.curso = ?")
	List<PedidoIsencaoDisciplinas> findByCurso(Curso curso);

	@Query("SELECT i FROM ItemPedidoIsencaoDisciplina i WHERE i.id= ?")
	ItemPedidoIsencaoDisciplina findItemIsencaoById(Long id);

	/**
	 * 
	 * @param departamento
	 * 
	 * @return todos os alunos que solicitaram isenção para ao menos uma
	 *         disciplina de <code>departamento</code>
	 */
	@Query("SELECT DISTINCT pi.aluno FROM PedidoIsencaoDisciplinas pi JOIN pi.itens i JOIN i.disciplina d "
			+ "WHERE d IN (SELECT disc FROM Departamento depto JOIN depto.disciplinas disc WHERE depto = ? ORDER BY pi.aluno.pessoa.nome)")
	List<Aluno> findAlunosSolicitantesByDepartamento(Departamento departamento);

	@Query("SELECT DISTINCT i FROM PedidoIsencaoDisciplinas pi JOIN pi.itens i JOIN i.disciplina d "
			+ "WHERE pi.aluno = ?2 AND d IN (SELECT disc FROM Departamento depto JOIN depto.disciplinas disc WHERE depto = ?1)")
	List<ItemPedidoIsencaoDisciplina> findItensByDepartamentoAndAluno(Departamento departamento, Aluno aluno);
}
