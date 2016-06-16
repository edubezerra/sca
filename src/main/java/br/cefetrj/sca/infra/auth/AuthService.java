package br.cefetrj.sca.infra.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthService {

	@Autowired
	private IAutenticador authDao;

	public String getRemoteLoginResponse(String username,
			String password) {
		return authDao.getRemoteLoginResponse(username, password);
	}
}
