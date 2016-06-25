package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.usuarios.Usuario;

public interface UsuarioRepositorio extends JpaRepository<Usuario, Serializable> {

//	@Query("select u from Usuario u where u.login=?1 and u.password=?2")
//	Usuario login(String login, String password);
//
//	Usuario findByLoginAndPassword(String login, String password);

	Usuario findUsuarioByLogin(String login);

	Usuario findUsuarioByNome(String nome);
}
