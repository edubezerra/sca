<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport"
		content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		
	<title>SCA - Análise de Registros de Atividade Complementar</title>
	
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
	
		function enableSearchButton(flag) {
			$("#btn-search").prop("disabled", flag);
		}
		
		/*var json = "<h4>Ajax Response</h4><pre>"
					+ JSON.stringify(data, null, 4) + "</pre>";
			$('#feedback').html(json);*/		
		function display(data) {
			if(data.length == 0){
				$("#lista").html("Não há registros de atividades complementares com os critérios pesquisados!");
			}
			else{//TODO
				$.each(data.Item,
						function(registro){						
							$("#lista_registros").html(
															
							);
							alert(data.value+":"+data.text);
				             var div_data="<option value="+data.value+">"+data.text+"</option>";
				            alert(div_data);
				            $(div_data).appendTo('#ch_user1'); 
			            }
						function(registro) {
			                var table = $('#display'), row = null, data = null;
			                $.each(data, function(id, obj) {
			                    row = $('<tr></tr>'); // build a row
			                        $.each(obj, function(colIndex, property) {
			                            data = $('<td></td>', { //build a td element
			                                        text : property[colIndex]
			                                    // assign the value from the iterated json property
			                                    }).appendTo(row);
			                        });
			                    });
			                row.appendTo(table); //finally append the row to the table
			            }
				);
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
								<i class="fa fa-search"></i>Buscar</button>
	          			</td>
	          		</tr>
          		</table>
			</form>
		</div>
		
		<div class="row" id="lista_registros">
			<h4><b>Registros de atividade complementar:</b></h4>
			<div class="vcenter well" id="lista">						
				<table class="table text-center">
					<thead>
						<tr>
							<td><b>Código da Atividade</b></td>
							<td><b>Descrição</b></td>
							<td><b>Carga Horária</b></td>
							<td><b>Comprovante</b></td>
							<td><b>Data de Solicitação</b></td>
							<td><b>Status</b></td>																	
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${requestScope.registrosAtiv}" var="registro" >
							<c:choose>
								<c:when test="${registro.estado ne 'SUBMETIDO'}">
									<tr class="btn btn-default" onclick="escondeMostra('infoAnalise/${registro.idRegistro}');">
								</c:when>
								<c:otherwise>
									<tr>
								</c:otherwise>
							</c:choose>
							  <td>${registro.codigoAtividade}</td>
					          <td class="text-left">
					          	<div style="overflow:auto;width:300px;height:50px;">
					          		${registro.descricao}
								</div>
					          </td>
					          <td>${registro.cargaHoraria}</td>
					          <td>
					          	<c:if test="${registro.documento != null}">
									<form action="${pageContext.request.contextPath}/registroAtividades/downloadFile" method="POST" target="_blank">
						          		<input type="hidden" name="IdReg" value="${registro.idRegistro}">
										<button type="submit" class="btn btn-default" title="Download">
											<i class="fa fa-download"></i>
										</button>
									</form>
								</c:if>
					          </td>
					          <td>${registro.dataSolicitacao}</td>
					          <td>
					          	<c:set var="classeStatus" scope="page">
									<c:choose>
										<c:when test="${registro.estado eq 'INDEFERIDO'}">
											text-danger
										</c:when>
										<c:when test="${registro.estado eq 'DEFERIDO'}">
											text-success
										</c:when>
										<c:when test="${registro.estado eq 'EM_ANÁLISE'}">
											text-warning
										</c:when>
										<c:when test="${registro.estado eq 'SUBMETIDO'}">
											text-primary
										</c:when>
									</c:choose>
								</c:set>
								<p class="${classeStatus}"><b>${registro.estado}</b></p>
					          </td>
					          <td>
					          	<c:if test="${registro.podeSerCancelado}">
									<form action="${pageContext.request.contextPath}/registroAtividades/removeRegistroAtividade" method="POST">
						          		<input type="hidden" name="idReg" value="${registro.idRegistro}">
										<button type="submit" class="btn btn-default" title="Cancelar submissão">
											<i class="fa fa-trash-o"></i></button>
									</form>
								</c:if>						          	
							  </td>
					        </tr>
					        <tr id="infoAnalise/${registro.idRegistro}" style="display:none">
					        	<td></td>
					        	<td colspan="5">
						        	<table>
						        		<tr>
							        		<td><b>Professor Avaliador</b></td>
											<td><b>Data de Análise</b></td>
										</tr>
										<tr>
											<td>${registro.nomeAvaliador}</td>
											<td>${registro.dataAnalise}</td>
										</tr>
										<tr>
											<td><b>Justificativa</b></td>
											<td></td>
										</tr>
										<tr>
											<td colspan="2">${registro.justificativa}</td>
										</tr>
						        	</table>
					        	</td>						        	
					        </tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		
		<a class="btn btn-default" href="${pageContext.request.contextPath}/registroAtividades/menuPrincipal">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>
</html>
