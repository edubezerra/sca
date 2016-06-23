package br.cefetrj.sca.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.cefetrj.sca.dominio.Aluno;
import br.cefetrj.sca.dominio.Disciplina;
import br.cefetrj.sca.dominio.EnumSituacaoAvaliacao;
import br.cefetrj.sca.dominio.HistoricoEscolar;
import br.cefetrj.sca.dominio.ItemHistoricoEscolar;
import br.cefetrj.sca.dominio.PeriodoLetivo;
import br.cefetrj.sca.dominio.repositories.AlunoRepositorio;
import br.cefetrj.sca.dominio.repositories.DisciplinaRepositorio;
import br.cefetrj.sca.service.util.RelatorioUtil;

@Component
public class RelatorioReprovacaoDisciplinaService {

	@Autowired
	AlunoRepositorio alunoRepo;
	
	@Autowired
	DisciplinaRepositorio disciplinaRepo;
	
	private HashMap<PeriodoLetivo, List<Aluno>> alunosPorSemestre = new HashMap<>();
	
	public String createDataResponse(String codigoDisciplina){
		alunosPorSemestre.clear();
		/* lista contendo os codigos das disciplina de pesquisa e suas equivalentes*/
		List<String> codigosDisciplina = new ArrayList<>();
		/* lista de disciplinas equivalentes a disciplina de pesquisa */
		List<Disciplina> dEquivalentes;
		/* lista contendo todas as disciplinas */
		List<Disciplina> todasDisciplinas;
		/* disciplina buscada para relatorio */
		Disciplina disciplinaOriginal;
		/* lista contendo os alunos que cursaram as disciplinas */
		List<Aluno> alunoList;
		
		/* busca a disciplina enviada como parametro */
		disciplinaOriginal = disciplinaRepo.findDisciplinaByCodigo(codigoDisciplina);
		
		if(disciplinaOriginal == null) 
		{
			throw new IllegalArgumentException("O código " + codigoDisciplina + " não pertence a nenhuma disciplina cadastrada.");
		}
		
		/* busca as disciplinas equivalentes àquela disciplina de parametro */
		dEquivalentes = disciplinaRepo.findDisciplinasEquivalentesByCodigo(codigoDisciplina);
		
		/* armazena na lista de codigos de disciplina*/
		for(Disciplina disc: dEquivalentes) {
			codigosDisciplina.add(disc.getCodigo());
		}
		codigosDisciplina.add(disciplinaOriginal.getCodigo());
		
		/* monta a lista contendo todas as disciplinas */
		todasDisciplinas = new ArrayList<>(dEquivalentes);
		todasDisciplinas.add(disciplinaOriginal);
		
		/* busca os alunos que cursaram as disciplinas */
		alunoList = alunoRepo.getAlunosByDisciplinaCursadaList(codigosDisciplina);
		
		/* para cada aluno, se ele possui um item de historico escolar para aquela disciplina, com a situacao de reprovado, 
		 * 	adiciona na lista de alunos reprovados*/
		for(Aluno a: alunoList) {
			HistoricoEscolar historico = a.getHistorico();
			
			for(Disciplina d: todasDisciplinas) {
				for(ItemHistoricoEscolar item : historico.getItemHistoricoByDisciplina(d)) {
					EnumSituacaoAvaliacao avalItem = item.getSituacao();
					
					if(avalItem.equals(EnumSituacaoAvaliacao.REPROVADO_POR_MEDIA)
							|| avalItem.equals(EnumSituacaoAvaliacao.REPROVADO_POR_FALTAS)
							|| avalItem.equals(EnumSituacaoAvaliacao.REPROVADO_SEM_NOTA)) {
						List<Aluno> alunos = alunosPorSemestre.get(item.getPeriodoLetivo());
						
						if(alunos == null)
							alunos = new ArrayList<>();
						
						if(!alunos.contains(a))
						{
							alunos.add(a);
							alunosPorSemestre.put(item.getPeriodoLetivo(), alunos);
						}
					}
				}
			}
		}
		
		return RelatorioUtil.createJSONResponse(alunosPorSemestre);
	}
}
