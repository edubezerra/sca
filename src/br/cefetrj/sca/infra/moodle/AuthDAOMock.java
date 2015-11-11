package br.cefetrj.sca.infra.moodle;

import br.cefetrj.sca.infra.IAuthDAO;

public class AuthDAOMock implements IAuthDAO{

	
	
	@Override
	public String getRemoteLoginResponse(String username, String password) {
		
		return null;
	}

}
