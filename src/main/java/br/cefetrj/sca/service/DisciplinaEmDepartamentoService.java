package br.cefetrj.sca.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.cefetrj.sca.dominio.Departamento;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.repositories.DepartamentoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;

@Service
public class DisciplinaEmDepartamentoService {

	@Autowired
	DisciplinaRepositorio disciplinaRepo;

	@Autowired
	DepartamentoRepositorio departamentoRepo;

	public void alocarDisciplinas(Map<String, String> lotacoes) {
		for (String idDisciplina : lotacoes.keySet()) {

			Long id = Long.parseLong(idDisciplina);
			
			Disciplina disciplina = disciplinaRepo.findOne(id);
			String siglaDepartamento = lotacoes.get(idDisciplina);
			Departamento novaLotacao, antigaLotacao;

			novaLotacao = departamentoRepo
					.findDepartamentoBySigla(siglaDepartamento);

			List<Departamento> departamentos = departamentoRepo.findAll();

			for (Departamento d : departamentos) {
				if (d.getDisciplinas().contains(disciplina)) {
					d.removerDisciplina(disciplina);
					antigaLotacao = d;
					departamentoRepo.save(antigaLotacao);
					break;
				}
			}

			novaLotacao.addDisciplina(disciplina);

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

	public List<Disciplina> findDisciplinas() {
		return disciplinaRepo.findAll();
	}

	public HashMap<Long, String> findAlocacoesDisciplinas() {
		HashMap<Long, String> lotacoes = new HashMap<>();
		List<Disciplina> disciplinas = disciplinaRepo.findAll();
		List<Departamento> departamentos = departamentoRepo.findAll();
		for (Disciplina disciplina : disciplinas) {
			boolean alocado = false;
			for (Departamento departamento : departamentos) {
				if (departamento.getDisciplinas().contains(disciplina)) {
					lotacoes.put(disciplina.getId(), departamento.getSigla());
					alocado = true;
					break;
				}
			}
			if (!alocado) {
				lotacoes.put(disciplina.getId(), null);
			}
		}
		return lotacoes;
	}
}
