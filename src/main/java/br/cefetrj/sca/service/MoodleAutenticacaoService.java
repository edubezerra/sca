package br.cefetrj.sca.service;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.infra.auth.AuthService;

@Component("moodleAuth")
public class MoodleAutenticacaoService implements AutenticacaoService {

	protected Logger logger = Logger.getLogger(MoodleAutenticacaoService.class
			.getName());

	@Autowired
	private AuthService authRepository;

	@Autowired
	private AlunoRepositorio alunoRepo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * br.cefetrj.sca.service.AutenticacaoService#autentica(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public void autentica(String cpf, String senha) {

		if (cpf == null || cpf.isEmpty()) {
			throw new IllegalArgumentException("Forneça o CPF!");
		}

		if (senha == null || senha.isEmpty()) {
			throw new IllegalArgumentException("Forneça a senha!");
		}

		// remote login
		String response = authRepository.getRemoteLoginResponse(cpf, senha);

		if (response == null || response.isEmpty()) {
			logger.log(Level.SEVERE, "Nenhum resposta recebida.");
			throw new IllegalArgumentException("Usuário não reconhecido.");
		}

		if (!response.equals(cpf)) {
			logger.log(Level.SEVERE, "Resposta inesperada: " + response);
			String error = "Seu usuário não está registrado. "
					+ "Entre em contato com o administrador do sistema.";
			throw new IllegalArgumentException(error);
		}

		Aluno aluno = alunoRepo.findAlunoByCpf(cpf);

		// local user exists?
		if (aluno == null) {
			String error = "Seu usuário não está registrado. "
					+ "Entre em contato com o administrador do sistema.";
			throw new IllegalArgumentException(error);
		}
	}
	
	public static void main(String[] args) {
		MoodleAutenticacaoService main = new MoodleAutenticacaoService();
		main.authRepository = new AuthService();
		main.autentica("usuarioeic", "usuario@EIC2010");
		System.out.println("Done!");
	}

	@Override
	public boolean autenticaUsuario(String login, String senha) {
		return false;
	}

	@Override
	public boolean isAluno(String login) {
		return false;
	}

	@Override
	public boolean isProfessor(String login) {
		return false;
	}
}
