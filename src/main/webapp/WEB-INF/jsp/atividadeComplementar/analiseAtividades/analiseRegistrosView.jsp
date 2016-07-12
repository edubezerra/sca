<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport"
		content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		
	<title>SCA - Análise de Registros de Atividade Complementar</title>
	
	<link href="//netdna.bootstrapcdn.com/font-awesome/4.1.0/css/font-awesome.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
		media="screen" rel="stylesheet" type="text/css" />
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/app.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/vendor/font-awesome/css/font-awesome.min.css">
    
    <!-- Required for tablesorter, bootstrap-popup and jquery.popconfirm-->
    <script src="http://code.jquery.com/jquery-1.12.1.min.js"></script>
    
    <!-- Bootstrap stylesheet -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/tablesorter/css/bootstrap.min.css">
	<!-- bootstrap widget theme -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/tablesorter/css/theme.bootstrap_2.css">
	<!-- tablesorter plugin -->
	<script src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.js"></script>
	<!-- tablesorter widget file - loaded after the plugin -->
	<script src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.widgets.js"></script>
	<!-- pager plugin -->
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/tablesorter/css/jquery.tablesorter.pager.css">
	<script src="${pageContext.request.contextPath}/resources/tablesorter/js/jquery.tablesorter.pager.js"></script>
     
    <!-- Include bootstrap-popup and jquery.popconfirm -->
    <script src="http://netdna.bootstrapcdn.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
    <script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap-popup.min.js"></script>
	<script type="text/javascript" 
		src="${pageContext.request.contextPath}/resources/bootstrap/js/jquery.popconfirm.js"></script>
    
	<!-- Include Date Range Picker -->
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/bootstrap/js/moment.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/bootstrap/js/daterangepicker.js"></script>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/bootstrap/css/daterangepicker.css" />

    <style>
  		th{cursor:pointer;}
  	</style>
  	
  	<script type="text/javascript">
		$(function() {		
		    function cb(start, end) {
		        $('#daterange span').html(start.format('D MMMM, YYYY') + ' - ' + end.format('D MMMM, YYYY'));
		        $('#startDate').val(start.format('DD/MM/YYYY'));
		        $('#endDate').val(end.format('DD/MM/YYYY'));
		    }
		    //cb(moment().subtract(29, 'days'), moment());
		
		    $('#daterange').daterangepicker({
		    	"locale": {
		            "format": "DD/MM/YYYY",
		            "separator": " - ",
		            "applyLabel": "Aplicar",
		            "cancelLabel": "Cancelar",
		            "fromLabel": "De",
		            "toLabel": "Até",
		            "customRangeLabel": "Intervalo Customizado",
		            "daysOfWeek": [
		                "D","S","T","Q","Q","S","S"
		            ],
		            "monthNames": [
		                "Janeiro","Fevereiro","Março","Abril","Maio","Junho","Julho","Agosto","Setembro","Outubro","Novembro","Dezembro"
		            ],
		            "firstDay": 1
		        },
		        ranges: {
		           'Hoje': [moment(), moment()],
		           'Últimos 7 Dias': [moment().subtract(6, 'days'), moment()],
		           'Últimos 30 Dias': [moment().subtract(29, 'days'), moment()],
		           'Este Mês': [moment().startOf('month'), moment().endOf('month')],		           
		           'Último Mês': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
		           'Este ano': [moment().startOf('year'), moment().endOf('year')],
		           'Desde o início': [moment("01/01/2012"),moment()],
		        }
		    }, cb);
		});
	</script>
  	
  	<script>
	  	function setTablesorter() {
	  		
		  	  // call the tablesorter plugin and apply the uitheme widget
		  	  var $table1 = $("#tabela_registros").tablesorter({
		  	    // this will apply the bootstrap theme if "uitheme" widget is included
		  	    // the widgetOptions.uitheme is no longer required to be set
		  	    theme : "bootstrap",
		
		  	    widthFixed: true,
		  		// this is the default setting
		        cssChildRow: "tablesorter-childRow",
		
		  	    headerTemplate : '{content} {icon}', // new in v2.7. Needed to add the bootstrap icon!
		  	    
		  		// sort on the fifth column and first column, order desc and asc 
		        sortList: [[2,1],[0,0]],
		
		  	    // widget code contained in the jquery.tablesorter.widgets.js file
		  	    // use the zebra stripe widget if you plan on hiding any rows (filter widget)
		  	    widgets : [ "uitheme", "filter", "zebra", "pager" ],
		
		  	    widgetOptions : {
		  	      // If there are child rows in the table (rows with class name from "cssChildRow" option)
	              // and this option is true and a match is found anywhere in the child row, then it will make that row
	              // visible; default is false
	              filter_childRows: true,
		  	      // using the default zebra striping class name, so it actually isn't included in the theme variable above
		  	      // this is ONLY needed for bootstrap theming if you are using the filter widget, because rows are hidden
		  	      zebra : ["even", "odd"],
		
		  	      // hide the filter row when not active
		  	      filter_hideFilters : true,
		  	      
		          // output default: '{page}/{totalPages}'
		          // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
		          pager_output: '{startRow} - {endRow} / {filteredRows} ({totalRows})',
	
		          pager_removeRows: false,
	
		          // class name applied to filter row and each input
		          filter_cssFilter  : 'tablesorter-filter',
		          // search from beginning
		          filter_startsWith : false,
		          // Set this option to false to make the searches case sensitive
		          filter_ignoreCase : true
		        }
		  	  })
		  	  .tablesorterPager({
		
		  	    // target the pager markup - see the HTML block below
		  	    container: $(".ts-pager"),
		
		  	    // target the pager page select dropdown - choose a page
		  	    cssGoto  : ".pagenum",
		
		  	    // remove rows from the table to speed up the sort of large tables.
		  	    // setting this to false, only hides the non-visible rows; needed if you plan to add/remove rows with the pager enabled.
		  	    removeRows: false,
		
		  	    // output string - default is '{page}/{totalPages}';
		  	    // possible variables: {page}, {totalPages}, {filteredPages}, {startRow}, {endRow}, {filteredRows} and {totalRows}
		  	    output: '{startRow} - {endRow} / {filteredRows} ({totalRows})'
		
		  	  });
		  	  
		  	  // hide child rows - get in the habit of not using .hide()
		  	  // See http://jsfiddle.net/Mottie/u507846y/ & https://github.com/jquery/jquery/issues/1767
		  	  // and https://github.com/jquery/jquery/issues/2308
		  	  // This won't be a problem in jQuery v3.0+
		  	  $table1.find( '.tablesorter-childRow td' ).addClass( 'hidden' );
	
		  	  // Toggle child row content (td), not hiding the row since we are using rowspan
		  	  // Using delegate because the pager plugin rebuilds the table after each page change
		  	  // "delegate" works in jQuery 1.4.2+; use "live" back to v1.3; for older jQuery - SOL
		  	  $table1.delegate( '.toggle', 'click' ,function() {
		  	    // use "nextUntil" to toggle multiple child rows
		  	    // toggle table cells instead of the row
		  	    $( this )
		  	      .closest( 'tr' )
		  	      .nextUntil( 'tr.tablesorter-hasChildRow' )
		  	      .find( 'td' )
		  	      .toggleClass( 'hidden' );
		  	    return false;
		  	  });
	
		  	  // Toggle filter_childRows option
		  	  $( 'button.toggle-combined' ).click( function() {
		  	    var wo = $table1[0].config.widgetOptions,
		  	    o = !wo.filter_childRows;
		  	    wo.filter_childRows = o;
		  	    $( '.state1' ).html( o.toString() );
		  	    // update filter; include false parameter to force a new search
		  	    $table1.trigger( 'search', false );
		  	    return false;
		  	  });
	  	}
	  	
	  	function setConfirmations() {
		  	  $("[data-toggle='confirmation']").popConfirm();
		}
  	</script>
    
    <script>  
	    function escondeMostra(x){  
	        if(document.getElementById(x).style.display == "none" || document.getElementById(x).style.display == ""){  
	            document.getElementById(x).style.display = "table-row";  
	        }  
	        else{  
	            document.getElementById(x).style.display = "none";  
	        }  
	    }  
	</script>
	
	<script>
		function getVersoesCurso() {
			var optionSelected = $("#siglaCurso").find("option:selected");
            var siglaCurso  = optionSelected.val();
            
			var search = {}
			search["siglaCurso"] = siglaCurso;
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${pageContext.request.contextPath}/analiseAtividades/obterVersoesCurso",
				data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);					
					$("#numeroVersao").empty();
		            if (siglaCurso == "") {
		            	$('<option>').val("").text("Todas as versões").appendTo($("#numeroVersao"));
		                $("#numeroVersao").attr("disabled", true);
		            } else {		            	
		                var numeroVersao = data;
		                $('<option>').val("").text("Todas as versões").appendTo($("#numeroVersao"));
		                for (var i = 0; i < numeroVersao.length; i++) {
		                	$('<option>').val(numeroVersao[i]).text(numeroVersao[i]).appendTo($("#numeroVersao"));
		                }
		                $("#numeroVersao").attr("disabled", false);
		            }
		        },
				error : function(e) {
					console.log("ERROR: ", e);
					display(e);},
				done : function(e) {
					console.log("DONE");
					enableSearchButton(true);}
			});	
		}
	</script>
	<script>
		function promptJustificativa(idRegistro,matriculaAluno,novoStatus){
			$.bs.popup.prompt({
				  title: 'Justificativa',
				  info: 'Informe a justificativa para o indeferimento.'
				}, function(dialogE, message) {
					atualizaStatusAtividade(idRegistro,matriculaAluno,novoStatus,message);
					dialogE.modal('hide');
				});			
		}
		function atualizaStatusAtividade(idRegistro,matriculaAluno,novoStatus,justificativa) {
			
			var search = {}
			search["matriculaProf"] = String("${requestScope.dadosAnaliseAtividades.matriculaProfessor}");
			search["siglaCurso"] = String($("#siglaCurso").val());
			search["numeroVersao"] = String($("#numeroVersao").val());
			search["status"] = String($("#status").val());
			search["startDate"] = $('#startDate').val();
			search["endDate"] = $('#endDate').val();
			search["novoStatus"] =  String(novoStatus);
			search["idRegistro"] = String(idRegistro);
			search["matriculaAluno"] = String(matriculaAluno);
			search["justificativa"] = String(justificativa);
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${pageContext.request.contextPath}/analiseAtividades/defineStatusAtividade",
				data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);
					$("#statusError").html("");
					display(data);},
				error : function(e) {
					console.log("ERROR: ", e);
					$("#statusError").html("Registro de atividade complementar não pode ser deferido! Carga horária máxima atingida!");
					searchRegistrosAtividade();}
			});	
		}
	</script>
	
	<script>
		jQuery(document).ready(function($) {	
			$("#search-form").submit(function(event) {	
				// Disble the search button
				enableSearchButton(false);
				// Prevent the form from submitting via the browser.
				event.preventDefault();	
				searchRegistrosAtividade();	
			});
		});
	
		function searchRegistrosAtividade() {	
			var search = {}
			search["matriculaProf"] = String("${requestScope.dadosAnaliseAtividades.matriculaProfessor}");
			search["siglaCurso"] = $("#siglaCurso").val();
			search["numeroVersao"] = $("#numeroVersao").val();
			search["status"] = $("#status").val();
			search["startDate"] = $('#startDate').val();
			search["endDate"] = $('#endDate').val();
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${pageContext.request.contextPath}/analiseAtividades/listarAtividades",
				data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);
					display(data);},
				error : function(e) {
					console.log("ERROR: ", e);
					$("#lista_registros").html(JSON.stringify(e));},
				done : function(e) {
					console.log("DONE");
					enableSearchButton(true);}
			});	
		}
	</script>
	
	<script>
		function enableSearchButton(flag) {
			$("#btn-search").prop("disabled", flag);
		}
		
		function display(data) {
			if(data.length == 0){
				$("#lista_registros").html("Não há registros de atividades complementares com os critérios pesquisados!");
			}
			else{
				var table_data = "<table id='tabela_registros' class='table text-center tablesorter'>";
				table_data = table_data+"<thead>";
				table_data = table_data+"<tr>";
          		table_data = table_data+
          			"<th class='text-left'><b>Aluno</b></th>"+
					"<th class='text-left'><b>Atividade</b></th>"+
					"<th class='filter-select filter-exact' data-placeholder='Escolha uma data'><b>Data de Solicitação</b></th>"+
					"<th class='filter-select filter-exact' data-placeholder='Escolha uma data'><b>Data de Análise</b></th>"+
          			"</tr>";
          		table_data = table_data+"</thead>";
          		table_data = table_data+"<tbody id='corpo_lista_registros'>";
				
				var registros = data;
                for (var i = 0; i < registros.length; i++) {
                	             	
                	table_data = table_data+
						"<tr class='tablesorter-hasChildRow toggle' style='cursor: pointer; cursor: hand;'>";
					table_data = table_data+"<td class='text-left'>"+registros[i].nomeAluno+"</td>";
					table_data = table_data+
						"<td class='text-left'><div style='overflow:auto;width:100%;height:40px;'>"+
						registros[i].descrAtividade+"</div></td>";
	          		table_data = table_data+"<td>"+registros[i].dataSolicitacao+"</td>";
	          		table_data = table_data+"<td>"+registros[i].dataAnalise+"</td>";
	          		table_data = table_data+"</tr>";
	          		
	          		table_data = table_data+
	          			"<tr class='tablesorter-childRow'>";
	          		table_data = table_data+"<td colspan='4'><table class='table text-center'><thead><tr>";
	          		table_data = table_data+
	          			"<td class='text-left'><b>Descrição</b></td>"+
						"<td><b>Carga Horária</b></td>"+
						"<td><b>Comprovante</b></td>"+
						"<td><b>Status: ";
	          		switch(registros[i].estado){
			          	case "INDEFERIDO":
			          		var classeStatus = "text-danger";break;
			          	case "DEFERIDO":
			          		var classeStatus = "text-success";break;
			          	case "EM_ANÁLISE":
			          		var classeStatus = "text-warning";break;
			          	case "SUBMETIDO":
			          		var classeStatus = "text-primary";break;
		          	}
					table_data = table_data+"<span class='"+classeStatus+"'>"+registros[i].estado+"</b></span>";
					table_data = table_data+
						"</td>";
					table_data = table_data+"</tr></thead>";
	          			
	          		table_data = table_data+"<tbody><tr>";
	          		table_data = table_data+
						"<td class='text-left'><div style='overflow:auto;width:100%;height:30px;'>"+
						registros[i].descricao+"</div></td>";
	          		table_data = table_data+"<td>"+registros[i].cargaHoraria+"</td>";
	          		table_data = table_data+"<td>";
	          		if(registros[i].temDocumento){
	          			table_data = table_data+
	          			"<form style='height:10px;' action='${pageContext.request.contextPath}/analiseAtividades/downloadFile' method='POST' target='_blank'>"+
		          		"<input type='hidden' name='IdReg' value='"+registros[i].idRegistro+"'>"+
		          		"<input type='hidden' name='matriculaAluno' value='"+registros[i].matriculaAluno+"'>"+
						"<button type='submit' class='btn btn-default' title='Download'>"+
							"<i class='fa fa-download'></i>"+
						"</button>"+
						"</form>";
	          		}
	          		table_data = table_data+"</td>";
	          		
	          		table_data = table_data+"<td>";
		          	switch(registros[i].estado){
			          	case "INDEFERIDO":
			          		table_data = table_data+
			          		"<button "+
							  "onclick='atualizaStatusAtividade(\""+registros[i].idRegistro+"\",\""+registros[i].matriculaAluno+"\",\"EM_ANÁLISE\",\"\")' "+
							  "class='btn btn-default' title='Atualizar status para: EM ANÁLISE'>"+
								"<i class='fa fa-question text-warning'></i>"+
							"</button>"+
							"<button "+
							  "class='btn btn-default'"+
							  "data-toggle='confirmation'"+
							  "data-confirm-title='Confirmação' data-confirm-content='Atualizar status para DEFERIDO?'"+
							  "data-confirm-placement='top' data-confirm-yesBtn='Sim' data-confirm-noBtn='Não'"+
							  "onClick='atualizaStatusAtividade(\""+registros[i].idRegistro+"\",\""+registros[i].matriculaAluno+"\",\"DEFERIDO\",\"\")'"+
							">"+
								"<i class='fa fa-check text-success'></i>"+
							"</button>";break;
			          	case "EM_ANÁLISE":
			          		table_data = table_data+
			          		"<button "+
							  "class='btn btn-default'"+
							  "data-toggle='confirmation'"+
							  "data-confirm-title='Confirmação' data-confirm-content='Atualizar status para INDEFERIDO?'"+
							  "data-confirm-placement='top' data-confirm-yesBtn='Sim' data-confirm-noBtn='Não'"+
							  "onClick='promptJustificativa(\""+registros[i].idRegistro+"\",\""+registros[i].matriculaAluno+"\",\"INDEFERIDO\")'"+
							">"+
								"<i class='fa fa-times text-danger'></i>"+
							"</button>"+
							"<button "+
							  "class='btn btn-default'"+
							  "data-toggle='confirmation'"+
							  "data-confirm-title='Confirmação' data-confirm-content='Atualizar status para DEFERIDO?'"+
							  "data-confirm-placement='top' data-confirm-yesBtn='Sim' data-confirm-noBtn='Não'"+
							  "onClick='atualizaStatusAtividade(\""+registros[i].idRegistro+"\",\""+registros[i].matriculaAluno+"\",\"DEFERIDO\",\"\")'"+
							">"+
								"<i class='fa fa-check text-success'></i>"+
							"</button>";break;
			          	case "SUBMETIDO":
			          		table_data = table_data+
		          			"<button "+
							  "onclick='atualizaStatusAtividade(\""+registros[i].idRegistro+"\",\""+registros[i].matriculaAluno+"\",\"EM_ANÁLISE\",\"\")' "+
							  "class='btn btn-default' title='Atualizar status para: EM ANÁLISE'>"+
								"<i class='fa fa-question text-warning'></i>"+
							"</button>"+
							"<button "+
							  "class='btn btn-default'"+
							  "data-toggle='confirmation'"+
							  "data-confirm-title='Confirmação' data-confirm-content='Atualizar status para INDEFERIDO?'"+
							  "data-confirm-placement='top' data-confirm-yesBtn='Sim' data-confirm-noBtn='Não'"+
							  "onClick='promptJustificativa(\""+registros[i].idRegistro+"\",\""+registros[i].matriculaAluno+"\",\"INDEFERIDO\")'"+
							">"+
								"<i class='fa fa-times text-danger'></i>"+
							"</button>"+
							"<button "+
							  "class='btn btn-default'"+
							  "data-toggle='confirmation'"+
							  "data-confirm-title='Confirmação' data-confirm-content='Atualizar status para DEFERIDO?'"+
							  "data-confirm-placement='top' data-confirm-yesBtn='Sim' data-confirm-noBtn='Não'"+
							  "onClick='atualizaStatusAtividade(\""+registros[i].idRegistro+"\",\""+registros[i].matriculaAluno+"\",\"DEFERIDO\",\"\")'"+
							">"+
								"<i class='fa fa-check text-success'></i>"+
							"</button>";break;
		          	}
					table_data = table_data+"</td>";
					table_data = table_data+"</tr>";
					
					if(registros[i].estado != "SUBMETIDO"){
						table_data = table_data+"<tr>";
						table_data = table_data+"<td class='text-left' colspan='4'><b>Professor Avaliador:</b> "+registros[i].nomeAvaliador+"</td>";
					    table_data = table_data+"</tr>";
					    if(registros[i].estado == "INDEFERIDO"){
						    table_data = table_data+"<tr>";
						    table_data = table_data+"<td class='text-left' colspan='4'><b>Justificativa:</b> "+registros[i].justificativa+"</td>";
							table_data = table_data+"</tr>";
					    }
					}
					table_data = table_data+"</tbody></table></tr>";		            		        
                }
                table_data = table_data+"</tbody>";
                table_data = table_data+"<tfoot>";
          		table_data = table_data+
          			"<tr>"+
          			"<th class='text-left'><b>Aluno</b></th>"+
					"<th class='text-left'><b>Atividade</b></th>"+
					"<th><b>Data de Solicitação</b></th>"+
					"<th><b>Data de Análise</b></th>"+
          			"</tr>";
          		table_data = table_data+
                "<tr>"+
                  "<th colspan='7' class='ts-pager form-horizontal'>"+
                    "<button type='button' class='btn first'><i class='icon-step-backward glyphicon glyphicon-step-backward'></i></button>"+
                    "<button type='button' class='btn prev'><i class='icon-arrow-left glyphicon glyphicon-backward'></i></button>"+
                    "<span class='pagedisplay'></span>"+
                    "<button type='button' class='btn next'><i class='icon-arrow-right glyphicon glyphicon-forward'></i></button>"+
                    "<button type='button' class='btn last'><i class='icon-step-forward glyphicon glyphicon-step-forward'></i></button>"+
                    "<select class='pagesize input-mini' title='Selecione o tamanho da página'>"+
                      "<option selected='selected' value='10'>10</option>"+
                      "<option value='20'>20</option>"+
                      "<option value='30'>30</option>"+
                      "<option value='40'>40</option>"+
                    "</select>"+
                    "<select class='pagenum input-mini' title='Selecione a página'></select>"+
                  "</th></tr>";
                table_data = table_data+"</tfoot>";
				table_data = table_data+"</table>";				
          		
				$("#lista_registros").html(table_data);				
				setTablesorter();
				setConfirmations();
				$("#tabela_registros").tablesorter();
			}
		}
	</script>
</head>
<body class="lista-solicitacoes">

	<div class="container">
		<div class="row text-center">
			<h3>Análise de Registros de Atividades Complementares</h3>
		</div>
		<hr/>
		<div class="row">
			<h5><b>Professor:</b> <c:out value="${requestScope.dadosAnaliseAtividades.nomeProfessor}"></c:out> (Matrícula: <c:out value="${requestScope.dadosAnaliseAtividades.matriculaProfessor}"></c:out>)</h5>
		</div>
		<br/>
		<c:if test="${requestScope.sucesso != null}">
			<div class="row">
				<span class="label label-success">${requestScope.sucesso}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.error != null}">
			<div class="row">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
		</c:if>
		<c:if test="${requestScope.info != null}">
			<div class="row">
				<span class="label label-info">${requestScope.info}</span>
			</div>
		</c:if>
		
		<div id="feedback"></div>
		
		<div class="row">
			<form id="search-form">
          		<table class="table text-center">
	          		<tr>
	          			<td>
	          				<select class="form-control input" id="siglaCurso" onchange="getVersoesCurso()">
	          					<option value="">Todos os cursos</option>
	          					<c:forEach items="${requestScope.dadosAnaliseAtividades.siglaCursos}" var="siglaCurso">
							  		<option value="${siglaCurso}">${siglaCurso}</option>
							  	</c:forEach>
							</select>
	          			</td>
	          			<td>
	          				<select class="form-control input" id="numeroVersao" disabled>
	          					<option value="">Todas as versões</option>
							</select>
	          			</td>
	          			<td>
	          				<select class="form-control input" id="status">
	          					<option value="">Todos os status de registro</option>
	          					<c:forEach items="${requestScope.dadosAnaliseAtividades.status}" var="status" >
							  		<option value="${status}">${status}</option>
							  	</c:forEach>
							</select>
	          			</td>
	          			<td class="text-left">
	          				<div id="daterange" title="Restringir intervalo" class="form-control input pull-right">
							    <i class="glyphicon glyphicon-calendar fa fa-calendar"></i>&nbsp;
							    <span>Desde o início</span> <b class="caret"></b>
							</div>
							<input type="hidden" id="startDate" name="startDate" value="">
							<input type="hidden" id="endDate" name="endDate" value="">
	          			</td>
	          			<td>
	          				<button id="btn-search" type="submit" class="btn btn-default" title="Buscar registros">
								<i class="fa fa-search"></i> Buscar</button>
	          			</td>
	          		</tr>
          		</table>
			</form>
		</div>
		
		<div class="row">
			<span id="statusError" class="label label-danger"></span>
		</div>
		
		<div class="row">
			<h4><b>Registros de atividade complementar:</b></h4>
			<div class="vcenter well" id="lista_registros">
				Pesquise os registros de atividade complementar a serem analisados.			
			</div>
		</div>
		
		<a class="btn btn-default" href="${pageContext.request.contextPath}/registroAtividades/menuPrincipal">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>
</html>
