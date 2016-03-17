package br.cefetrj.sca.service.util;

public class RemoteLoginResponse {

	private String token;
	private String error;

	public RemoteLoginResponse() {
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public RemoteLoginResponse(String token, String error) {
		this.token = token;
		this.error = error;
	}
}
