package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.Departamento;

public interface DepartamentoRepositorio extends
		JpaRepository<Departamento, Serializable> {

	public Departamento findDepartamentoById(Long id);

	public Departamento findDepartamentoByNome(String siglaDepartamento);
}
