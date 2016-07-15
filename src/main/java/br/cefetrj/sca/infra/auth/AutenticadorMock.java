package br.cefetrj.sca.infra.auth;

public class AutenticadorMock implements EstrategiaAutenticacao {

	@Override
	public String getRemoteLoginResponse(String username, String password) {
		return username;
	}

}
