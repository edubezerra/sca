package br.cefetrj.sca.dominio.isencoes;

import java.util.ArrayList;
import java.util.List;

import br.cefetrj.sca.dominio.Disciplina;

public class FichaIsencao {
	String nomeAluno;
	String matriculaAluno;
	List<Disciplina> disciplinasDisponiveis = new ArrayList<>();
}
