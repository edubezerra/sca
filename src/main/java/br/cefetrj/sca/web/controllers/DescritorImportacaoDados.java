package br.cefetrj.sca.web.controllers;

import java.util.HashMap;

public class DescritorImportacaoDados {
	HashMap<Long, String> descritorImportadorMap = new HashMap<Long, String>();

	public DescritorImportacaoDados() {
		Long i = 1L;

		descritorImportadorMap.put(i++, "Grade Curriculares (curso, versões, disciplinas)");
		descritorImportadorMap.put(i++, "Históricos Escolares");
		descritorImportadorMap.put(i++, "Turmas (com alunos e inscrições)");
		descritorImportadorMap.put(i++, "Questionário de Avaliação de Professores");
		descritorImportadorMap.put(i++, "Usuários com perfil de aluno");
		descritorImportadorMap.put(i++, "AtividadesComp");

		/**
		 * N.B.: Importadores abaixo ainda não foram migrados para a plataforma
		 * WEB.
		 */
		descritorImportadorMap.put(i++, "PreReqs");
		descritorImportadorMap.put(i++, "Alunos");
		descritorImportadorMap.put(i++, "Professores");
		descritorImportadorMap.put(i++, "Departamentos");
		descritorImportadorMap.put(i++, "importadorAlocacoesProfessoresEmTurmas");
		descritorImportadorMap.put(i++, "HabilitacoesParaProfessor");
		descritorImportadorMap.put(i++, "AlocacoesProfessoresEmDepartamentos");
		descritorImportadorMap.put(i++, "AlocacoesDisciplinasEmDepartamentos");
	}

	public HashMap<Long, String> getDescritorImportadorMap() {
		return descritorImportadorMap;
	}
}
