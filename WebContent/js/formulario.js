


$(document).ready(function(){
	$('input[type=radio][name=question-1]').change(function() {
        if (this.value == "3" || this.value == "4") {
            $("#divHideSlice").hide("slow");
        }else{
        	$("#divHideSlice").show("slow");
        }
    });
	
	$('#btnSend').on("click", function(e){
		var checkBox = $('#chkAllow')[0];
		if(!checkBox.checked){
			alert("Para enviar as respostas, é necessário marcar a opção 'Autorizo a divulgação dos meus contatos pessoais junto às empresas.'");
			e.preventDefault();
		}
	});
	
});