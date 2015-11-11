package br.cefetrj.sca.service;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.PeriodoAvaliacoesTurmas;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositorio.ProfessorRepositorio;

@Component
public class MockAutenticacaoService implements AutenticacaoService {

	@Autowired
	private AlunoRepositorio alunoRepo;
	
	@Autowired
	private ProfessorRepositorio professorRepo;

	@Override
	public void autentica(String login, String senha) {
		
		Professor professor = professorRepo.getProfessor(login);
		Aluno aluno = alunoRepo.getByCPF(login);

		// local user exists?
		if (aluno == null || professor == null) {
			String error = "Seu usu�rio n�o est� registrado. "
					+ "Entre em contato com o administrador do sistema.";
			throw new IllegalArgumentException(error);
		}
	}
	
	public String autenticaUsuario(String login, String senha, HttpSession session, Model model){
		if(isAluno(login)) {
			session.setAttribute("cpf", login);
			PeriodoAvaliacoesTurmas periodoAvaliacao = PeriodoAvaliacoesTurmas
					.getInstance();
			model.addAttribute("periodoLetivo",
					periodoAvaliacao.getSemestreLetivo());
			return "/avaliacaoTurma/menuPrincipalView";
		} else if(isProfessor(login)){
			session.setAttribute("matricula", login);
			return "/professor/menuPrincipalProfessorView";
		} else {
			String error = "Seu usu�rio n�o est� registrado. "
					+ "Entre em contato com o administrador do sistema.";
			throw new IllegalArgumentException(error);
		}
	}
	
	public boolean isProfessor(String login){
		Professor professor = professorRepo.getProfessor(login);
		if(professor != null){
			return true;
		}
		return false;	
	}
	
	public boolean isAluno(String login){
		Aluno aluno = alunoRepo.getByCPF(login);
		if(aluno != null){
			return true;
		}
		return false;
	}
	
	
	
	
}
