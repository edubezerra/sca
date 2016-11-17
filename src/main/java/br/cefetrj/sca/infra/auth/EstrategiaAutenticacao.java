package br.cefetrj.sca.infra.auth;

import org.springframework.stereotype.Component;

@Component
public interface EstrategiaAutenticacao {
	String getRemoteLoginResponse(String username, String password);
}
