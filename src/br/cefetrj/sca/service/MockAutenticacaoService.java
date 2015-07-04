package br.cefetrj.sca.service;

import org.springframework.stereotype.Component;

@Component
public class MockAutenticacaoService implements AutenticacaoService {

	@Override
	public void autentica(String cpf, String senha) {
		// FAZ NADA.
	}
}
