package br.cefetrj.sca.service;

import java.util.List;

import br.cefetrj.sca.dominio.usuarios.Usuario;


public interface UsuarioService {
	
	Usuario findById(int id);
	
	void saveUser(Usuario user);
	
	void updateUser(Usuario user);
	
	List<Usuario> findAllUsers(); 
}