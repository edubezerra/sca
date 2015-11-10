


$(document).ready(function(){
	$('input[type=radio][name=question-1]').change(function() {
        if (this.value == "3" || this.value == "4") {
            $("#divHideSlice").hide("slow");
        }else{
        	$("#divHideSlice").show("slow");
        }
    });
});