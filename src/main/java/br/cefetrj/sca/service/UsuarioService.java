package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.repositories.UsuarioRepositorio;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@Service
@Transactional
public class UsuarioService {

	@Autowired
	private UsuarioRepositorio repositorio;

	public void adicionarUsuario(Usuario usr) {
		repositorio.save(usr);
	}

	/*
	 * Uma vez que o método está sendo executado em um contexto transacional,
	 * NÃO há necessidade de chamar explicitamente o método save do JPA. Basta
	 * buscar a entidade a partir de banco de dados e atualizá-lo com valores
	 * apropriados dentro da transação. Essa entidade será atualizada no db uma
	 * vez que a transação termine.
	 */
	public void atualizarUsuario(Usuario user) {
		Usuario entity = repositorio.findOne(user.getId());
		if (entity != null) {
			entity.setLogin(user.getLogin());
			entity.setNome(user.getNome());
			entity.setEmail(user.getEmail());
			entity.setUserProfiles(user.getUserProfiles());
		}
	}

	public List<Usuario> findAll() {
		return repositorio.findAll();
	}

//	public Usuario create(Usuario user) {
//		return repositorio.save(user);
//	}
//
//	public Usuario findUserById(int id) {
//		return repositorio.findOne(id);
//	}
//
//	public Usuario update(Usuario user) {
//		return repositorio.save(user);
//	}
//
	public void deleteUser(String login) {
		Usuario usr = repositorio.findUsuarioByLogin(login);
		repositorio.delete(usr);
	}

	public Usuario findUsuarioByLogin(String login) {
		return repositorio.findUsuarioByLogin(login);
	}

	public boolean isLoginJaExistente(int id, String login) {
		return repositorio.findUsuarioByLogin(login) != null;
	}
}
