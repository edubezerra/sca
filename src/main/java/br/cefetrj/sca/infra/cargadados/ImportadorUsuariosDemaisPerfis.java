package br.cefetrj.sca.infra.cargadados;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.repositories.PerfilUsuarioRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;
import br.cefetrj.sca.dominio.repositories.UsuarioRepositorio;
import br.cefetrj.sca.dominio.usuarios.PerfilUsuario;
import br.cefetrj.sca.dominio.usuarios.Usuario;

@Component
public class ImportadorUsuariosDemaisPerfis {
	@Autowired
	private ProfessorRepositorio professorRepositorio;

	@Autowired
	private UsuarioRepositorio usuarioRepositorio;

	@Autowired
	private PerfilUsuarioRepositorio perfilUsuarioRepositorio;

	public String run() {

		StringBuilder response = new StringBuilder();

		PerfilUsuario perfilEgresso = perfilUsuarioRepositorio
				.getPerfilUsuarioByNome("ROLE_EGRESSO");
		if (perfilEgresso == null) {
			perfilEgresso = new PerfilUsuario("ROLE_EGRESSO");
			perfilUsuarioRepositorio.save(perfilEgresso);
		}

		PerfilUsuario perfilAdmin = perfilUsuarioRepositorio
				.getPerfilUsuarioByNome("ROLE_ADMIN");
		if (perfilAdmin == null) {
			perfilAdmin = new PerfilUsuario("ROLE_ADMIN");
			perfilUsuarioRepositorio.save(perfilAdmin);
		}

		PerfilUsuario perfilCoordenadorCurso = perfilUsuarioRepositorio
				.getPerfilUsuarioByNome("ROLE_COORDENADOR_CURSO");
		if (perfilCoordenadorCurso == null) {
			perfilCoordenadorCurso = new PerfilUsuario("ROLE_COORDENADOR_CURSO");
			perfilUsuarioRepositorio.save(perfilCoordenadorCurso);
		}

		PerfilUsuario perfilSECAD = perfilUsuarioRepositorio
				.getPerfilUsuarioByNome("ROLE_SECAD");
		if (perfilSECAD == null) {
			perfilSECAD = new PerfilUsuario("ROLE_SECAD");
			perfilUsuarioRepositorio.save(perfilSECAD);
		}

		PerfilUsuario perfilCoordenadorAC = perfilUsuarioRepositorio
				.getPerfilUsuarioByNome("ROLE_COORDENADOR_ATIVIDADES");
		if (perfilCoordenadorAC == null) {
			perfilCoordenadorAC = new PerfilUsuario(
					"ROLE_COORDENADOR_ATIVIDADES");
			perfilUsuarioRepositorio.save(perfilCoordenadorAC);
		}

		String login = "1223216BCC";
		Usuario usuario = usuarioRepositorio.findUsuarioByLogin(login);
		if (usuario != null) {
			usuario.getUserProfiles().add(perfilEgresso);
			usuarioRepositorio.save(usuario);
		}

		login = "1506449";
		usuario = usuarioRepositorio.findUsuarioByLogin(login);
		if (usuario != null) {
			usuario.getUserProfiles().add(perfilEgresso);
			usuario.getUserProfiles().add(perfilAdmin);
			usuario.getUserProfiles().add(perfilCoordenadorCurso);
			usuario.getUserProfiles().add(perfilSECAD);
			usuarioRepositorio.save(usuario);
		}

		login = "1604711";
		usuario = usuarioRepositorio.findUsuarioByLogin(login);
		if (usuario != null) {
			usuario.getUserProfiles().add(perfilCoordenadorAC);
			usuarioRepositorio.save(usuario);
		}
		
		return response.toString();
	}
}
