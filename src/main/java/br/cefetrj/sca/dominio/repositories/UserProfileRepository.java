package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;

public interface UserProfileRepository extends JpaRepository<PerfilUsuario, Serializable> {

	PerfilUsuario findByType(String type);

}
