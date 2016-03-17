package br.cefetrj.sca.infra.auth;

public interface IAuthDAO {
	String getRemoteLoginResponse(String username, String password);
}
