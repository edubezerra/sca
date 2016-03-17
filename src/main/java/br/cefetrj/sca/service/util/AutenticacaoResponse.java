package br.cefetrj.sca.service.util;

public class AutenticacaoResponse {

	String sessao;
	String erro;

	public String getSessao() {
		return sessao;
	}

	public void setSessao(String sessao) {
		this.sessao = sessao;
	}

	public String getErro() {
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}
}
