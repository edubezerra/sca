package br.cefetrj.sca.dominio.repositories;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.cefetrj.sca.dominio.User;

public interface UserRepository extends JpaRepository<User, Serializable> {

	@Query("select u from User u where u.login=?1 and u.password=?2")
	User login(String login, String password);

	User findByLoginAndPassword(String login, String password);

	User findUserByLogin(String login);

	User findUserByName(String name);

}
