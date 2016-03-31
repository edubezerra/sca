package br.cefetrj.sca.dominio;

import static org.junit.Assert.*;

import org.junit.Test;

import br.cefetrj.sca.dominio.PeriodoLetivo.EnumPeriodo;

public class SemestreLetivoTest {

	@Test
	public void testCriacaoCorreta() {
		PeriodoLetivo sl1 = new PeriodoLetivo(2015, EnumPeriodo.PRIMEIRO);
		assertEquals(sl1.getAno(), new Integer(2015));
		assertEquals(sl1.getPeriodo(), EnumPeriodo.PRIMEIRO);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCriacaoSemInformacaoNecessaria() {
		new PeriodoLetivo(2015, null);
		new PeriodoLetivo(null, EnumPeriodo.PRIMEIRO);
	}

	@Test
	public void testEquals() {
		PeriodoLetivo sl1 = new PeriodoLetivo(2015, EnumPeriodo.PRIMEIRO);
		PeriodoLetivo sl2 = new PeriodoLetivo(2015, EnumPeriodo.PRIMEIRO);
		assertEquals(sl1, sl2);
	}

	@Test
	public void testProximo() {
		PeriodoLetivo sl1 = new PeriodoLetivo(2015, EnumPeriodo.SEGUNDO);
		PeriodoLetivo sl2 = new PeriodoLetivo(2016, EnumPeriodo.PRIMEIRO);
		assertEquals(sl1.proximo(), sl2);
	}
}
