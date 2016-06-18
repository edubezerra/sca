package br.cefetrj.sca.infra.auth;

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
	AutenticadorMoodle autenticador;
	
	@Test
	public void m() {
		System.out.println(autenticador.getRemoteLoginResponse("ebezerra", ""));
	}
}
