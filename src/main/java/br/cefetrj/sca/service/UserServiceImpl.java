package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.repositories.UsuarioRepository;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@Service("usuarioService")
@Transactional
public class UserServiceImpl implements UsuarioService {

	@Autowired
	private UsuarioRepository dao;

	public Usuario findById(int id) {
		return dao.findOne(id);
	}

	public void saveUser(Usuario user) {
		dao.save(user);
	}

	/*
	 * Since the method is running with Transaction, No need to call hibernate
	 * update explicitly. Just fetch the entity from db and update it with
	 * proper values within transaction. It will be updated in db once
	 * transaction ends.
	 */
	public void updateUser(Usuario user) {
		Usuario entity = dao.findOne(user.getId());
		if (entity != null) {
			entity.setSsoId(user.getSsoId());
			entity.setPassword(user.getPassword());
			entity.setFirstName(user.getFirstName());
			entity.setLastName(user.getLastName());
			entity.setEmail(user.getEmail());
			entity.setUserProfiles(user.getUserProfiles());
		}
	}

	public List<Usuario> findAllUsers() {
		return dao.findAll();
	}

}
