package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.BlocoEquivalencia;

public interface BlocoEquivalenciaRepositorio extends JpaRepository<BlocoEquivalencia, Serializable>{

}
