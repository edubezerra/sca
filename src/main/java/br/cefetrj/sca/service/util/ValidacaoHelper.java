package br.cefetrj.sca.service.util;

import java.util.List;

public class ValidacaoHelper {
	public ValidacaoResponse validaRespostasEgresso(List<Long> respostas, String questionarioTipo){
		
		if (questionarioTipo.equals("Graduacao")) {
			if (respostas.contains(3) || respostas.contains(4)) {
				if (respostas.size() < 5) {
					return new ValidacaoResponse(false, "É necessário responder o questionário inteiro.");
				}
			}
			
			if (respostas.contains(52) && respostas.size() < 15) {
				return new ValidacaoResponse(false, "É necessário responder o questionário inteiro.");
			}
			
			if(respostas.contains(53) && respostas.size() == 13){
				return new ValidacaoResponse(true, "");
			}
			
			if(respostas.size() < 15) {
					return new ValidacaoResponse(false, "É necessário responder o questionário inteiro.");
			}		
			
			return new ValidacaoResponse(true, "");
		}
		else {
			if (respostas.contains(3) || respostas.contains(4)) {
				if (respostas.size() < 4) {
					return new ValidacaoResponse(false, "É necessário responder o questionário inteiro.");
				}
			}
			else {							
				if (respostas.contains(52) && respostas.size() < 14) {
					return new ValidacaoResponse(false, "É necessário responder o questionário inteiro.");
				}
				
				if(respostas.contains(53) && respostas.size() == 13){
					return new ValidacaoResponse(true, "");
				}
				
				if(respostas.size() < 14) {
						return new ValidacaoResponse(false, "É necessário responder o questionário inteiro.");
				}			
			}
			
			return new ValidacaoResponse(true, "");
		}
	}
}
