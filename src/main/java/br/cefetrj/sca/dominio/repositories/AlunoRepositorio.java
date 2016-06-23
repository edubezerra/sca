package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.EnumSituacaoAvaliacao;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.VersaoCurso;

public interface AlunoRepositorio extends JpaRepository<Aluno, Serializable> {
	
	Aluno findAlunoByMatricula(String matricula);

	@Query("SELECT a from Aluno a WHERE a.pessoa.cpf = ?1")
	Aluno findAlunoByCpf(String cpf);
	
	@Query("from Aluno a where a.versaoCurso = ?1")
	List<Aluno> findAllByVersaoCurso(VersaoCurso versaoCurso);	

	@Query("SELECT a from Aluno a")
	List<Aluno> getAllAlunos();
	
	@Query("SELECT a from Aluno a WHERE a.versaoCurso.curso.sigla = ?1 " + " AND a.matricula LIKE ?2% ORDER BY a.pessoa.nome")
	List<Aluno> getAlunosByCursoEPeriodo(String siglaCurso, String periodo);
	
	@Query("SELECT distinct a from Aluno a JOIN a.historico h JOIN a.historico.itens i WHERE i.disciplina.codigo = ?1")
	List<Aluno> getAlunosByDisciplinasCursadas(String codigoDisciplina);
	
	@Query("SELECT distinct a from Aluno a JOIN a.historico h JOIN a.historico.itens i WHERE i.disciplina.codigo IN ?1")
	List<Aluno> getAlunosByDisciplinaCursadaList(List<String> codigosDisciplina);
	
	@Query("SELECT a from Aluno a JOIN a.historico h JOIN a.historico.itens i"
			+ " WHERE a.matricula = ?1"
			+ " AND i.periodoLetivo = ?2"
			+ " AND i.situacao = ?3"
			+ " AND i.disciplina.codigo = ?4"
			+ " AND i.disciplina.versaoCurso.numero = ?5"
			+ " AND i.disciplina.versaoCurso.curso.sigla = ?6")
	Aluno getAlunoByInfoHistoricoEscolar(String matricula, PeriodoLetivo periodoLetivo, 
			EnumSituacaoAvaliacao situacao, String codigoDisciplina, String numeroVersao, String codigoCurso);
}