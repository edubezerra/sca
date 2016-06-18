package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.QuestionarioAvaliacaoDocente;

public interface FormularioAvaliacaoRepositorio extends
		JpaRepository<QuestionarioAvaliacaoDocente, Serializable> {

	public QuestionarioAvaliacaoDocente findFormularioAvaliacaoBySigla(String sigla);

}
