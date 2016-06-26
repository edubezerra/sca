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
import br.cefetrj.sca.dominio.VersaoCurso;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.CursoRepositorio;
import br.cefetrj.sca.service.util.RelatorioUtil;

@Component
public class RelatorioRetencaoService {

	@Autowired
	AlunoRepositorio alunoRepo;
	
	@Autowired
	CursoRepositorio cursoRepo;
	
    private Map<PeriodoLetivo, List<Aluno>> alunosPorSemestre = new HashMap<>();
    
    public String createDataResponse(String siglaCurso, String strPeriodoLetivo){
    	alunosPorSemestre.clear();
    	
    	/* iniciais da matricula */
		String periodoMatricula = RelatorioUtil.getPeriodoMatriculaByPeriodoString(strPeriodoLetivo);
		
		/* lista de alunos daquele periodo informado pelo coordenador */
		List<Aluno> alunosList = alunoRepo.getAlunosByCursoEPeriodo(siglaCurso, periodoMatricula);
		
		/* ano e periodo escolhido pelo coordenador */
        PeriodoLetivo periodoMinimo = RelatorioUtil.getPeriodoLetivoByPeriodoString(strPeriodoLetivo);
        
        /* qtd minima de periodos para se formar no curso */
        VersaoCurso versaoCursoDoPeriodo = alunosList.get(0).getVersaoCurso();
        Integer qtdPeriodoMinimo = versaoCursoDoPeriodo.getQtdPeriodoMinimo();

        if(qtdPeriodoMinimo == null) 
        {
        	throw new IllegalArgumentException("Não há informação sobre o período mínimo para a Versão Curso: " + versaoCursoDoPeriodo);
        }
        
        /* pegar o periodo minimo para aquela turma se formar */
        for(int i = 0; i < qtdPeriodoMinimo; i++){
        	periodoMinimo = periodoMinimo.proximo();
        }
        
        /* para cada aluno daquela turma */
        /* se houver algum registro no historico escolar */
        /* que seja maior do que o periodo minimo, considerar retencao */
		for(Aluno aluno: alunosList) {
			HistoricoEscolar hist = aluno.getHistorico();

			for(PeriodoLetivo periodo: hist.getPeriodosLetivosByItemHistoricoEscolar()){
				if(periodo.compareTo(periodoMinimo) >= 0){
					List<Aluno> alunos = alunosPorSemestre.get(periodo);
					
					if(alunos == null)
						alunos = new ArrayList<>();
					
					alunos.add(aluno);
					alunosPorSemestre.put(periodo, alunos);	
				}
			}
		}
		
		return RelatorioUtil.createJSONResponse(alunosPorSemestre);
	}
	
}

