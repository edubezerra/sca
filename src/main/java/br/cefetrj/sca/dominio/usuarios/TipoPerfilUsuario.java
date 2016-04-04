package br.cefetrj.sca.dominio.usuarios;

public enum TipoPerfilUsuario {
	ROLE_ALUNO("ROLE_ALUNO"),
	ROLE_PROFESSOR("ROLE_PROFESSOR"),
	ROLE_USER("ROLE_USER"),
	ROLE_COORDENADOR_CURSO("ROLE_COORDENADOR_CURSO"),
	ROLE_ADMIN("ROLE_ADMIN");
	
	String userProfileType;
	
	private TipoPerfilUsuario(String userProfileType){
		this.userProfileType = userProfileType;
	}
	
	public String getUserProfileType(){
		return userProfileType;
	}
	
}
