package br.cefetrj.sca.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Professor;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.ProfessorRepositorio;

@Service
public class ProfessoresEmDepartamentosService {

	@Autowired
	ProfessorRepositorio professorRepo;

	@Autowired
	DepartamentoRepositorio departamentoRepo;

	public void alocarProfessores(Map<String, String> lotacoes) {
		for (String matriculaProfessor : lotacoes.keySet()) {

			Professor professor = professorRepo
					.findProfessorByMatricula(matriculaProfessor);
			String siglaDepartamento = lotacoes.get(matriculaProfessor);
			Departamento novaLotacao, antigaLotacao;

			novaLotacao = departamentoRepo
					.findDepartamentoBySigla(siglaDepartamento);

			List<Departamento> departamentos = departamentoRepo.findAll();

			for (Departamento d : departamentos) {
				if (d.getProfessores().contains(professor)) {
					d.removerProfessor(professor);
					antigaLotacao = d;
					departamentoRepo.save(antigaLotacao);
					break;
				}
			}

			novaLotacao.addProfessor(professor);

			departamentoRepo.save(novaLotacao);
		}
	}

	public HashMap<String, String> listarDepartamentos() {
		List<Departamento> departamentos = departamentoRepo.findAll();
		HashMap<String, String> mapa = new HashMap<String, String>();
		for (Departamento departamento : departamentos) {
			mapa.put(departamento.getSigla(), departamento.getNome());
		}
		return mapa;
	}

	public List<Professor> findProfessores() {
		return professorRepo.findAll();
	}

	public HashMap<String, String> findLotacoesProfessores() {
		HashMap<String, String> lotacoes = new HashMap<String, String>();
		List<Professor> professores = professorRepo.findAll();
		List<Departamento> departamentos = departamentoRepo.findAll();
		for (Professor professor : professores) {
			boolean alocado = false;
			for (Departamento departamento : departamentos) {
				if (departamento.getProfessores().contains(professor)) {
					lotacoes.put(professor.getMatricula(),
							departamento.getSigla());
					alocado = true;
					break;
				}
			}
			if (!alocado) {
				lotacoes.put(professor.getMatricula(), null);
			}
		}
		return lotacoes;
	}
}
