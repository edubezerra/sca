package br.cefetrj.sca.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.repositorio.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositorio.AuthRepository;

@Component
public class MoodleAutenticacaoService implements AutenticacaoService {

	@Autowired
	private AuthRepository authRepository;

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
			 throw new IllegalArgumentException("Usuário não reconhecido.");
		}

		// if (response.getError() != null || response.getToken() == null
		// || response.getToken().trim().isEmpty()) {
		// String error = "Cannot authenticate user in remote auth service.";
		// error += "\nError: ";
		// error += ((response.getError() == null) ? ("Null") : (response
		// .getError()));
		// throw new IllegalArgumentException(error);
		// }

		Aluno aluno = alunoRepo.getByCPF(cpf);

		// local user exists?
		if (aluno == null) {
			String error = "Seu usuário não está registrado. "
					+ "Entre em contato com o administrador do sistema.";
			throw new IllegalArgumentException(error);
		}
	}
	/*
	 * public String logoutUser(String session_id) {
	 * 
	 * if (session_id == null || session_id.equals("")) { throw new
	 * IllegalArgumentException("Undefined session identifier."); }
	 * 
	 * // session identify a logged user if
	 * (!userRepository.existWithSessionId(session_id)) { throw new
	 * IllegalArgumentException("Invalid session identifier"); }
	 * 
	 * // logout User user = userRepository.getBySessionId(session_id);
	 * 
	 * user.setSession_id(null); userRepository.save(user);
	 * 
	 * return "User deauthenticated successfully."; }
	 */
}
