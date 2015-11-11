package br.cefetrj.sca.service;

public interface AutenticacaoService {

	public abstract void autentica(String cpf, String senha);

	boolean autenticaUsuario(String login, String senha);

	public abstract boolean isAluno(String login);

	public abstract boolean isProfessor(String login);

}