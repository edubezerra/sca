package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.FormularioAvaliacao;

public interface FormularioAvaliacaoRepositorio extends
		JpaRepository<FormularioAvaliacao, Serializable> {

	public FormularioAvaliacao findFormularioAvaliacaoBySigla(String sigla);

}
