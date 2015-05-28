package br.cefetrj.sca.dominio.repositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.infra.IAuthDAO;
import br.cefetrj.sca.service.util.RemoteLoginResponse;

@Component
public class AuthRepository {

	@Autowired
	private IAuthDAO authDao;

	public RemoteLoginResponse getRemoteLoginResponse(String username,
			String password) {
		return authDao.getRemoteLoginResponse(username, password);
	}
}
