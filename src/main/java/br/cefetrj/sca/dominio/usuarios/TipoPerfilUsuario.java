package br.cefetrj.sca.dominio.usuarios;

public enum TipoPerfilUsuario {
	USER("USER"),
	DBA("DBA"),
	ADMIN("ADMIN");
	
	String userProfileType;
	
	private TipoPerfilUsuario(String userProfileType){
		this.userProfileType = userProfileType;
	}
	
	public String getUserProfileType(){
		return userProfileType;
	}
	
}
