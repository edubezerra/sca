package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.PesquisaAvaliacao;

public interface PesquisaAvaliacaoRepositorio extends JpaRepository<PesquisaAvaliacao, Serializable> {

	PesquisaAvaliacao findByDescritor(String descritor);

}
