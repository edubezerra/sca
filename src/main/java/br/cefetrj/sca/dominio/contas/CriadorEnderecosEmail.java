package br.cefetrj.sca.dominio.contas;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class CriadorEnderecosEmail {

	private String removeAcentos(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}

	public Email criar(String nome, List<String> enderecosEmail) {
		return null;
	}

	public Email criar(String nome) {
		if (nome == null || nome.isEmpty()) {
			throw new IllegalArgumentException(
					"Nome não pode ser nulo ou vazio.");
		}
		nome = removeAcentos(nome);
		String[] partesNome = nome.split("\\s");

		if (partesNome.length < 2) {
			throw new IllegalArgumentException(
					"Nome completo deve ser composto do primeiro nome e ao menos um sobrenome: "
							+ nome);
		}

		String primeiroNome = partesNome[0].toLowerCase();
		String ultimoNome = partesNome[partesNome.length - 1].toLowerCase();
		return new Email(primeiroNome + "." + ultimoNome + "@cefet-rj.br");
	}

	public static void main(String[] args) {
		CriadorEnderecosEmail criador = new CriadorEnderecosEmail();
		Email email = criador.criar("Eduardo Bezerra da Silva");
		email = criador.criar("João Ruan Soares Minto");
		email = criador.criar("João Silva Minto");
		System.out.println(email.getEndereco());

		List<String> lista = new ArrayList<String>();
		String nome = "MARIA JOSÉ";
		// String nome2 = "MARIA ANA JOSÉ";
		lista.add(nome);
		email = criador.criar(nome, lista); // retorna maria.jose.i
	}

}
