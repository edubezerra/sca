package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.avaliacaoturma.Alternativa;

public interface AlternativaRepositorio extends
		JpaRepository<Alternativa, Serializable> {

}
