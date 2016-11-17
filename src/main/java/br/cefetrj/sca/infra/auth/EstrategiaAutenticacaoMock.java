package br.cefetrj.sca.infra.auth;

import org.springframework.stereotype.Component;

@Component("mockAuth")
public class EstrategiaAutenticacaoMock implements EstrategiaAutenticacao {

	@Override
	public String getRemoteLoginResponse(String username, String password) {
		return username;
	}

}
