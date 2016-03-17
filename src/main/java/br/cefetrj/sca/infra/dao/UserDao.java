/**
 * 
 */
package br.cefetrj.sca.infra.dao;

import java.util.List;

import br.cefetrj.sca.dominio.User;

public interface UserDao {

	public List<User> findAll();
	
	public User create(User user);
	
	public User findUserById(int id);

	public User login(String email, String password);
	
}
