package br.cefetrj.sca.service;

public interface AutenticacaoService {

	public abstract void autentica(String cpf, String senha);
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