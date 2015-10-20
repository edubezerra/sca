package misc.lojavirtual;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VendaTest {

	@Test
	public void totalVendaDeveCorrespondeAoTotalItens() {
		Venda venda = new Venda();
		Produto p1 = new Produto("Arroz", 1.0f);
		Produto p2 = new Produto("Feijão", 1.0f);
		Produto p3 = new Produto("Ovo", 1.0f);
		venda.registrarItem(p1, 10);
		venda.registrarItem(p2, 20);
		venda.registrarItem(p3, 30);
		assertEquals(venda.getTotal(), new Float(60.0));
		Float saldo = venda.registrarPagamento(30.0f);
		assertEquals(saldo, new Float(30.0));
	}
	
	@Test
	public void deveEstarQuitada() {
		Venda venda = new Venda();
		Produto p1 = new Produto("Arroz", 1.0f);
		Produto p2 = new Produto("Feijão", 1.0f);
		Produto p3 = new Produto("Ovo", 1.0f);
		venda.registrarItem(p1, 10);
		venda.registrarItem(p2, 20);
		venda.registrarItem(p3, 30);
		venda.registrarPagamento(60.0f);
		assertTrue(venda.estaQuitada());
	}
}
