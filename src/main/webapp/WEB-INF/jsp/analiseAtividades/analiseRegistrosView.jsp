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
    
    <script>  
	    function escondeMostra(x){  
	        if(document.getElementById(x).style.display == "none" || document.getElementById(x).style.display == ""){  
	            document.getElementById(x).style.display = "inline";  
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
		            	$('<option>').val("").text("Versão").appendTo($("#numeroVersao"));
		                $("#numeroVersao").attr("disabled", true);
		            } else {		            	
		                var numeroVersao = data;
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
		function atualizaStatusAtividade(idForm,novoStatus) {
			var search = {}
			search["siglaCurso"] = $("#siglaCurso").val();
			search["numeroVersao"] = $("#numeroVersao").val();
			search["status"] = $("#status").val();
			search["novoStatus"] = novoStatus;
			search["IdReg"] = $("#"+idForm+" IdReg").val();
			search["matriculaAluno"] = $("#"+idForm+" matriculaAluno").val();
			$.ajax({
				type : "POST",
				contentType : "application/json",
				url : "${pageContext.request.contextPath}/analiseAtividades/defineStatusAtividade",
				data : JSON.stringify(search),
				dataType : 'json',
				timeout : 100000,
				success : function(data) {
					console.log("SUCCESS: ", data);
					display(data);},
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
			search["siglaCurso"] = $("#siglaCurso").val();
			search["numeroVersao"] = $("#numeroVersao").val();
			search["status"] = $("#status").val();
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
					display(e);},
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
				var table_data = "<table class='table text-center'><thead><tr>";
				table_data = table_data+"<thead>";
				table_data = table_data+"<tr>";
          		table_data = table_data+
          			"<td class='text-left'><b>Aluno</b></td>"+
					"<td class='text-left'><b>Atividade</b></td>"+
					"<td><b>Data de Solicitação</b></td>"+
					"<td><b>Data de Análise</b></td>"+
          			"</tr></thead>";
          		table_data = table_data+"<tbody id='corpo_lista_registros'>";
				
				var registros = data;
                for (var i = 0; i < registros.length; i++) {
                	             	
                	table_data = table_data+
						"<tr class='btn btn-default' onclick='escondeMostra('infoAnalise/"+registros[i].idRegistro+"');'>";
					table_data = table_data+"<td class='text-left'>"+registros[i].nomeAluno+"</td>";
					table_data = table_data+
						"<td class='text-left'><div style='overflow:auto;width:300px;height:50px;'>"+
						registros[i].descrAtividade+"</div></td>";
	          		table_data = table_data+"<td>"+registros[i].dataSolicitacao+"</td>";
	          		table_data = table_data+"<td>"+registros[i].dataAnalise+"</td>";
	          		table_data = table_data+"</tr>";
	          		
	          		table_data = table_data+
	          			"<tr id='infoAnalise/"+registros[i].idRegistro+"' style='display:none'></tr>";
	          		table_data = table_data+"<td colspan='4'><table class='table text-center'><thead><tr>";
	          		table_data = table_data+
	          			"<td class='text-left'><b>Descrição</b></td>"+
						"<td><b>Carga Horária</b></td>"+
						"<td><b>Comprovante</b></td>"+
						"<td><b>Status: </b>";
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
					table_data = table_data+"<p class='"+classeStatus+"'><b>"+registros[i].estado+"</b></p>";
					table_data = table_data+
						"</td>";
					table_data = table_data+"</tr></thead>";
	          			
	          		table_data = table_data+"<tbody><tr>";
	          		table_data = table_data+
						"<td class='text-left'><div style='overflow:auto;width:300px;height:50px;'>"+
						registros[i].descricao+"</div></td>";
	          		table_data = table_data+"<td>"+registros[i].cargaHoraria+"</td>";
	          		table_data = table_data+"<td>";
	          		if(registros[i].documento != null){
	          			table_data = table_data+
	          			"<form action='${pageContext.request.contextPath}/registroAtividades/downloadFile' method='POST' target='_blank'>"+
		          		"<input type='hidden' name='IdReg' value='"+registros[i].idRegistro+"'>"+
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
		          			"<form id='emanalise-form/"+registros[i].idRegistro+"'>"+
			          		"<input type='hidden' name='IdReg' value='"+registros[i].idRegistro+"'>"+
			          		"<input type='hidden' name='matriculaAluno' value='"+registros[i].matriculaAluno+"'>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','EM_ANÁLISE')' class='btn btn-default' title='Atualizar status para: EM ANÁLISE'>"+
								"<i class='fa fa-question'></i>"+
							"</button>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','DEFERIDO')' class='btn btn-default' title='Atualizar status para: DEFERIDO'>"+
								"<i class='fa fa-check'></i>"+
							"</button>"+
							"</form>";break;
			          	case "DEFERIDO":
			          		table_data = table_data+
		          			"<form id='emanalise-form/"+registros[i].idRegistro+"'>"+
			          		"<input type='hidden' name='IdReg' value='"+registros[i].idRegistro+"'>"+
			          		"<input type='hidden' name='matriculaAluno' value='"+registros[i].matriculaAluno+"'>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','EM_ANÁLISE')' class='btn btn-default' title='Atualizar status para: EM ANÁLISE'>"+
								"<i class='fa fa-question'></i>"+
							"</button>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','INDEFERIDO')' class='btn btn-default' title='Atualizar status para: INDEFERIDO'>"+
								"<i class='fa fa-times'></i>"+
							"</button>"+
							"</form>";break;
			          	case "EM_ANÁLISE":
			          		table_data = table_data+
		          			"<form  id='indeferido-form/"+registros[i].idRegistro+"'>"+
			          		"<input type='hidden' name='IdReg' value='"+registros[i].idRegistro+"'>"+
			          		"<input type='hidden' name='matriculaAluno' value='"+registros[i].matriculaAluno+"'>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','INDEFERIDO')' class='btn btn-default' title='Atualizar status para: INDEFERIDO'>"+
								"<i class='fa fa-times'></i>"+
							"</button>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','DEFERIDO')' class='btn btn-default' title='Atualizar status para: DEFERIDO'>"+
								"<i class='fa fa-check'></i>"+
							"</button>"+
							"</form>";break;
			          	case "SUBMETIDO":
			          		table_data = table_data+
		          			"<form id='emanalise-form/"+registros[i].idRegistro+"'>"+
			          		"<input type='hidden' name='IdReg' value='"+registros[i].idRegistro+"'>"+
			          		"<input type='hidden' name='matriculaAluno' value='"+registros[i].matriculaAluno+"'>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','EM_ANÁLISE')' class='btn btn-default' title='Atualizar status para: EM ANÁLISE'>"+
								"<i class='fa fa-question'></i>"+
							"</button>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','INDEFERIDO')' class='btn btn-default' title='Atualizar status para: INDEFERIDO'>"+
								"<i class='fa fa-times'></i>"+
							"</button>"+
							"<button onclick='atualizaStatusAtividade('emanalise-form/"+registros[i].idRegistro+"','DEFERIDO')' class='btn btn-default' title='Atualizar status para: DEFERIDO'>"+
								"<i class='fa fa-check'></i>"+
							"</button>"+
							"</form>";break;
		          	}
					table_data = table_data+"</td>";
					table_data = table_data+"</tr>";
					
					if(registros[i].estado != "SUBMETIDO"){
						table_data = table_data+"<tr>";
						table_data = table_data+"<td colspan='4'><b>Professor Avaliador:</b> "+registros[i].nomeAvaliador+"</td>";
					    table_data = table_data+"</tr>";
					    table_data = table_data+"<tr>";
					    table_data = table_data+"<td><b>Justificativa:</b></td>";
					    table_data = table_data+"<td></td><td></td><td></td>";
						table_data = table_data+"</tr>";
						table_data = table_data+"<tr>";
						table_data = table_data+"<td colspan='4'>"+registros[i].justificativa+"</td>";
						table_data = table_data+"</tr>";
					}
					table_data = table_data+"</tbody></table>";		            		        
                }
                table_data = table_data+"</tbody>";
				table_data = table_data+"</table>";				
          		
				$("#lista_registros").html(table_data);
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
				<span class="label label-info">${requestScope.error}</span>
			</div>
		</c:if>
		
		<div id="feedback"></div>
		
		<div class="row">
			<form id="search-form">
          		<table class="table text-center">
	          		<tr>
	          			<td>
	          				<select class="form-control input" id="siglaCurso" onchange="getVersoesCurso()">
	          					<option value="">Curso</option>
	          					<c:forEach items="${requestScope.dadosAnaliseAtividades.siglaCursos}" var="siglaCurso">
							  		<option value="${siglaCurso}">${siglaCurso}</option>
							  	</c:forEach>
							</select>
	          			</td>
	          			<td>
	          				<select class="form-control input" id="numeroVersao" disabled>
	          					<option value="">Versão</option>
							</select>
	          			</td>
	          			<td>
	          				<select class="form-control input" id="status">
	          					<option value="">Status de registro</option>
	          					<c:forEach items="${requestScope.dadosAnaliseAtividades.status}" var="status" >
							  		<option value="${status}">${status}</option>
							  	</c:forEach>
							</select>
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
			<h4><b>Registros de atividade complementar:</b></h4>
			<div class="vcenter well" id="lista_registros">				
			</div>
		</div>
		
		<a class="btn btn-default" href="${pageContext.request.contextPath}/registroAtividades/menuPrincipal">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>
</html>
