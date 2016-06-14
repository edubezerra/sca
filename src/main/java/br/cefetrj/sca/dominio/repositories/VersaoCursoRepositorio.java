package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.VersaoCurso;

@Component
public interface VersaoCursoRepositorio extends JpaRepository<VersaoCurso, Serializable> {

	// public void adicionar(Disciplina d) {
	// dao.save(d);
	// }
	//
	// /**
	// *
	// * @param nome
	// * nome da disciplina
	// * @return a disciplina cujo nome foi passado como parâmetro, se existir;
	// * null em caso contrário.
	// */
	// public Disciplina getDisciplinaPorNome(String nome) {
	// return dao.findDisciplinaByNome(nome);
	// }
	//
	// public List<Disciplina> getDisciplinas() {
	// return dao.findAll();
	// }
	//
	// public boolean estaContidaEm(Set<Disciplina> preReqs,
	// Set<Disciplina> cursadas) {
	// return cursadas.containsAll(preReqs);
	// }
	//
	// public Disciplina getDisciplinaPorCodigo(String codigoDisciplina) {
	// return dao.findDisciplinaByCodigo(codigoDisciplina);
	// }

	@Query("from VersaoCurso versao where versao.numero = ?1 and versao.curso.sigla = ?2")
	public VersaoCurso findByNumeroEmCurso(String numeroVersao, String siglaCurso);
}
