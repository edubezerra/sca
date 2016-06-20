package br.cefetrj.sca.infra.auth;

public interface IAutenticador {
	String getRemoteLoginResponse(String username, String password);
}
