package br.cefetrj.sca.dominio;

import java.util.ArrayList;
import java.util.List;

public class ItensHorario {
	List<ItemHorario> itens = new ArrayList<ItemHorario>();

	public void adicionar(EnumDiaSemana dia, String fim, String inicio) {
		ItemHorario item = new ItemHorario(dia, fim, inicio);
		itens.add(item);
	}

	public ItemHorario getItem(EnumDiaSemana dia, String fim, String inicio) {
		ItemHorario item = new ItemHorario(dia, fim, inicio);
		for (ItemHorario itemHorario : itens) {
			if (itemHorario.equals(item)) {
				return itemHorario;
			}
		}
		return null;
	}
}
