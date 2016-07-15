package br.cefetrj.sca.infra.auth;

public interface EstrategiaAutenticacao {
	String getRemoteLoginResponse(String username, String password);
}
