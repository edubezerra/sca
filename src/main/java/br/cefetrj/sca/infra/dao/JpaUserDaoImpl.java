package br.cefetrj.sca.infra.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.usuarios.Usuario;

@Repository
@Transactional
public class JpaUserDaoImpl implements UserDao {
	@PersistenceContext
	private EntityManager em;

	@Override
	@Transactional(readOnly = true)
	public List<Usuario> findAll() {
		return em.createQuery("select u from User u", Usuario.class)
				.getResultList();
	}

	@Override
	@Transactional(readOnly = true)
	public Usuario findUserById(int id) {
		return em.find(Usuario.class, id);
	}

	@Override
	public Usuario create(Usuario user) {
		if (user.getId() <= 0) {
			em.persist(user);
		} else {
			user = em.merge(user);
		}
		return user;
	}

	@Override
	public Usuario login(String email, String password) {
		TypedQuery<Usuario> query = em.createQuery(
				"select u from User u where u.email=?1 and u.password=?2",
				Usuario.class);
		query.setParameter(1, email);
		query.setParameter(2, password);
		try {
			return query.getSingleResult();
		} catch (NonUniqueResultException | NoResultException e) {
			return null;
		}

	}

}
