package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.cefetrj.sca.dominio.repositories.UserProfileRepository;
import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;


@Service("userProfileService")
@Transactional
public class UserProfileServiceImpl implements UserProfileService{
	
	@Autowired
	UserProfileRepository dao;
	
	public PerfilUsuario findById(int id) {
		return dao.findOne(id);
	}

	public PerfilUsuario findByType(String type){
		return dao.findByType(type);
	}

	public List<PerfilUsuario> findAll() {
		return dao.findAll();
	}
}
