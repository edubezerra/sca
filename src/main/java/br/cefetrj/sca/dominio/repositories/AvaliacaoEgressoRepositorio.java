package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.AvaliacaoEgresso;

public interface AvaliacaoEgressoRepositorio extends
		JpaRepository<AvaliacaoEgresso, Serializable> {

	@Query("SELECT a FROM AvaliacaoEgresso a WHERE a.alunoAvaliador.pessoa.cpf = ?1")
	public AvaliacaoEgresso getAvaliacaoEgresso(String cpf);
}
