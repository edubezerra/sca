package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.usuarios.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Serializable> {

}
