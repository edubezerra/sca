$(document).ready( function() {
	
	exibirBotaoAddTurma();
	contaCaracteres();
	validate_fileupload();
	
});

function contaCaracteres() {
	var max = $("textarea#observacao").attr("maxlength");

	if($('textarea#observacao').val() <= 0){
		$('#max').text(max);
	}

	$('textarea#observacao').keyup(function(event){
		var chars = $('textarea#observacao').val().length;
		var charsleft = max - chars;
		$('#max').text(charsleft);

	});
}

function exibirBotaoAddTurma() {
	if(solicitacoesRestantes > 1){
		$(".add").show();
	}
}

function validate_fileupload(){
	var tamanhoMaximoComprovante = 10000000
	var tiposAceitos = ["application/pdf", "image/jpeg", "image/png"];
	$('#inputFile').change( function() {
		console.log(this);
		if(tiposAceitos.indexOf(this.files[0].type) < 0){
			limpaInputArquivo('O arquivo de comprovante deve ser no formato PDF, JPEG ou PNG', this);
		} else if(this.files[0].size > tamanhoMaximoComprovante){
			limpaInputArquivo('O arquivo de comprovante deve ter 10mb no máximo', this)
		} else {
			$('#tamanhoGrande').hide();
		}
	});
}

function limpaInputArquivo(texto, elemento){
	$(elemento).val("");
	$('#tamanhoGrande').text(texto);
	$('#tamanhoGrande').show();
}
