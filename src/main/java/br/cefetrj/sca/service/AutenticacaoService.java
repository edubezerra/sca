package br.cefetrj.sca.service;

public interface AutenticacaoService {

	void autentica(String matricula, String senha);

	boolean autenticaUsuario(String login, String senha);

	boolean isAluno(String login);

	boolean isProfessor(String login);

}