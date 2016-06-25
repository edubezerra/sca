package br.cefetrj.sca.infra.auth;

public class AutenticadorMock implements IAutenticador {

	@Override
	public String getRemoteLoginResponse(String username, String password) {
		return username;
	}

}
