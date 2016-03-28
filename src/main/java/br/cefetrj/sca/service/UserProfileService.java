package br.cefetrj.sca.service;

import java.util.List;

import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;


public interface UserProfileService {

	PerfilUsuario findById(int id);

	PerfilUsuario findByType(String type);
	
	List<PerfilUsuario> findAll();
	
}
