package br.cefetrj.sca.infra.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import br.cefetrj.sca.config.AppConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class AuthDAOMoodleTest {

	@Autowired
	EstrategiaAutenticacaoMoodle autenticador;

	@Test(timeout=1000000)
	public void deveAutenticarUsuarioValido() {
		assertEquals("058842457-93", autenticador.getRemoteLoginResponse(
				"058842457-93", "Aluno058842457-93"));
	}

	@Test
	public void naoDeveAutenticarUsuarioInvalido() {
		String resposta = autenticador.getRemoteLoginResponse("nonecziste", "");
		System.out.println(resposta);
		assertTrue(resposta.startsWith("\"error\""));
	}
}
