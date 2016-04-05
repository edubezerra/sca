/**
 * 
 */
package br.cefetrj.sca.infra.dao;

import java.util.List;

import br.cefetrj.sca.dominio.usuarios.Usuario;

public interface UserDao {

	public List<Usuario> findAll();
	
	public Usuario create(Usuario user);
	
	public Usuario findUserById(int id);

	public Usuario login(String email, String password);
	
}
