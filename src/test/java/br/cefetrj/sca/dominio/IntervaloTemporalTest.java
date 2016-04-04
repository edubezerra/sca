package br.cefetrj.sca.dominio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class IntervaloTemporalTest {

	@Test(expected=IllegalArgumentException.class)
	public void testHorasNegativas() {
		new IntervaloTemporal("-13:00", "14:30");
	}

	@Test(expected=IllegalArgumentException.class)
	public void testHorasMaioresQue12() {
		new IntervaloTemporal("1300:00", "1400:30");
	}

	@Test
	public void testDeveReportarPresencaColisao() {
		IntervaloTemporal tempo1 = new IntervaloTemporal("12:40", "13:30");
		IntervaloTemporal tempo2 = new IntervaloTemporal("13:00", "14:30");
		assertTrue(tempo1.colide(tempo2));
		assertTrue(tempo2.colide(tempo1));
	}
	@Test
	public void testDeveReportarAusenciaColisao() {
		IntervaloTemporal tempo1 = new IntervaloTemporal("12:40", "13:30");
		IntervaloTemporal tempo2 = new IntervaloTemporal("13:40", "14:30");
		assertFalse(tempo1.colide(tempo2));
	}
	
	@Test
	public void devemSerUniveis() {
		IntervaloTemporal tempo1 = new IntervaloTemporal("12:40", "13:30");
		IntervaloTemporal tempo2 = new IntervaloTemporal("13:30", "14:30");
		assertEquals(tempo1.unir(tempo2).getInicio(), "12:40");
		assertEquals(tempo1.unir(tempo2).getFim(), "14:30");
		assertEquals(tempo2.unir(tempo1).getInicio(), "12:40");
		assertEquals(tempo2.unir(tempo1).getFim(), "14:30");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void naoDevemSerUniveis() {
		IntervaloTemporal tempo1 = new IntervaloTemporal("12:40", "13:30");
		IntervaloTemporal tempo2 = new IntervaloTemporal("13:40", "14:30");
		tempo2.unir(tempo1);
	}
}
