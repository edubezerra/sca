package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;

import br.cefetrj.sca.dominio.usuarios.Usuario;

public interface UsuarioRepositorio extends
		JpaRepository<Usuario, Serializable> {

	Usuario findUsuarioByLogin(String login);

	Usuario findUsuarioByNome(String nome);
}
