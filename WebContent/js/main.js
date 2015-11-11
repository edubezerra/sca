$(document).ready( function() {
	
	exibirBotaoAddTurma();
	contaCaracteres();
	
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
