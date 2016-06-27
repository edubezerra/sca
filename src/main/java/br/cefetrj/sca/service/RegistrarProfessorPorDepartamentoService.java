package br.cefetrj.sca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Service
public class RegistrarProfessorPorDepartamentoService {

	@Autowired
	ProfessorRepositorio professorRepo;

	@Autowired
	DepartamentoRepositorio departamentoRepo;

	public List<Professor> findProfessores() {
		return professorRepo.findProfessores();
	}

	public List<Departamento> findDepartamentos() {
		return departamentoRepo.findDepartamentos();
	}

	public Departamento findDepartamentoByProfessor(String matriculaProfessor) {
		return departamentoRepo.findDepartamentoByProfessor(matriculaProfessor);
	}

	public Departamento findDepartamentoBySigla(String siglaDepartamento) {
		return departamentoRepo.findDepartamentoBySigla(siglaDepartamento);
	}
}
