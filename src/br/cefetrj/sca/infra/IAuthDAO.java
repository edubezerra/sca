package br.cefetrj.sca.infra;

public interface IAuthDAO {
	String getRemoteLoginResponse(String username, String password);
}
