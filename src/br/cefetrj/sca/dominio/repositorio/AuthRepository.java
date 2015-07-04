package br.cefetrj.sca.dominio.repositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.infra.IAuthDAO;

@Component
public class AuthRepository {

	@Autowired
	private IAuthDAO authDao;

	public String getRemoteLoginResponse(String username,
			String password) {
		return authDao.getRemoteLoginResponse(username, password);
	}
}
