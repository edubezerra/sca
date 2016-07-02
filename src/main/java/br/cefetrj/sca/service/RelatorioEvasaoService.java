package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.HistoricoEscolar;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.service.util.RelatorioUtil;

@Component
public class RelatorioEvasaoService {

	@Autowired
	AlunoRepositorio alunoRepo;
	
    private Map<PeriodoLetivo, List<Aluno>> alunosPorSemestre = new HashMap<>();

	public String createDataResponse(String siglaCurso, String strPeriodoLetivo){
		alunosPorSemestre.clear();
		/* ano e periodo escolhido pelo coordenador */
		PeriodoLetivo periodoLetivo = RelatorioUtil.getPeriodoLetivoByPeriodoString(strPeriodoLetivo);
		
		/* enquanto o periodo letivo escolhido não for maior ou igual ao periodo corrente 
		 * esta parte preenche o map de periodo letivo com os periodos desde o escolhido ateh o atual 
		 * */
		while(periodoLetivo.compareTo(PeriodoLetivo.PERIODO_CORRENTE) <= 0) {
			alunosPorSemestre.put(periodoLetivo, new ArrayList<>());
			periodoLetivo = periodoLetivo.proximo();
		}
		
		/* iniciais da matricula */
		String periodoMatricula = RelatorioUtil.getPeriodoMatriculaByPeriodoString(strPeriodoLetivo);
		
		/* lista de alunos daquele periodo informado pelo coordenador */
		List<Aluno> alunoList = alunoRepo.getAlunosByCursoEPeriodo(siglaCurso, periodoMatricula);
		
		for(Aluno aluno: alunoList) {
			HistoricoEscolar hist = aluno.getHistorico();
			
			for(PeriodoLetivo periodo: hist.getPeriodosLetivosByItemHistoricoEscolar()){
				List<Aluno> alunos = alunosPorSemestre.get(periodo);
				
				if(alunos == null)
					alunos = new ArrayList<>();
				
				alunos.add(aluno);
				alunosPorSemestre.put(periodo, alunos);
			}
		}
		
		return RelatorioUtil.createJSONResponse(alunosPorSemestre);
	}
}
