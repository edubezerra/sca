package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.matriculaforaprazo.AlocacacaoDisciplinasEmDepartamento;

public interface AlocacacaoDisciplinasEmDepartamentoRepositorio extends
		JpaRepository<AlocacacaoDisciplinasEmDepartamento, Serializable> {
	
	AlocacacaoDisciplinasEmDepartamento findAlocacacaoDisciplinasEmDepartamentoByDepartamento(Departamento depto);
}
