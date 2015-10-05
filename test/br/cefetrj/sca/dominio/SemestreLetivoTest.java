package br.cefetrj.sca.dominio;

import static org.junit.Assert.*;

import org.junit.Test;

import br.cefetrj.sca.dominio.SemestreLetivo.EnumPeriodo;

public class SemestreLetivoTest {

	@Test
	public void testCriacaoCorreta() {
		SemestreLetivo sl1 = new SemestreLetivo(2015, EnumPeriodo.PRIMEIRO);
		assertEquals(sl1.getAno(), new Integer(2015));
		assertEquals(sl1.getPeriodo(), EnumPeriodo.PRIMEIRO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCriacaoSemInformacaoNecessaria() {
		new SemestreLetivo(2015, null);
		new SemestreLetivo(null, EnumPeriodo.PRIMEIRO);
	}

	@Test
	public void testEquals() {
		SemestreLetivo sl1 = new SemestreLetivo(2015, EnumPeriodo.PRIMEIRO);
		SemestreLetivo sl2 = new SemestreLetivo(2015, EnumPeriodo.PRIMEIRO);
		assertEquals(sl1, sl2);
	}

	@Test
	public void testProximo() {
		SemestreLetivo sl1 = new SemestreLetivo(2015, EnumPeriodo.SEGUNDO);
		SemestreLetivo sl2 = new SemestreLetivo(2016, EnumPeriodo.PRIMEIRO);
		assertEquals(sl1.proximo(), sl2);
	}
}
