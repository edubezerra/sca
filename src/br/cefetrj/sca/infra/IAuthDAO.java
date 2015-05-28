package br.cefetrj.sca.infra;

import br.cefetrj.sca.service.util.RemoteLoginResponse;


public interface IAuthDAO {
	RemoteLoginResponse getRemoteLoginResponse(String username, String password);
}
