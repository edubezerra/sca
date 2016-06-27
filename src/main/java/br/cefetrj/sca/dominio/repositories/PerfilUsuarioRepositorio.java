package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;

public interface PerfilUsuarioRepositorio extends
		JpaRepository<PerfilUsuario, Serializable> {

	@Query("from PerfilUsuario u where u.type = ?1")
	PerfilUsuario getPerfilUsuarioByNome(String type);

}
