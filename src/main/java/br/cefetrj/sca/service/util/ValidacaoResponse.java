package br.cefetrj.sca.service.util;


public class ValidacaoResponse {
		private String message;
		private boolean isValid;
		public ValidacaoResponse(boolean isValid, String message){
			this.isValid = isValid;
			this.message = message;
		}
		
		public boolean isValid(){
			return isValid;
		}
		
		public String message(){
			return message;
		}
		
}
