<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
	<title>SCA - Registro de Atividades Complementares</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/vendor/font-awesome/css/font-awesome.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    
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
	
	<script type="text/javascript">   
		$(document).ready(function(){
		        $("#div2").hide()
				$("#job").change(
		        function(){
		        	var valor = $("option:selected", this).val();
		        	if(valor == '6'){
                       	$("#div2").show();                      
					}
					else{
						$("#div2").hide();  
					}		                        
		        });
		});
	</script>
</head>
<body class="lista-solicitacoes">

	<div class="container">
		<div class="row text-center">
			<h3>Análise de Registros de Atividades Complementares</h3>
		</div>
		<hr/>
		<div class="row">
			<h5><b>Professor:</b> <c:out value="${requestScope.nomeProfessor}"></c:out> (Matrícula: <c:out value="${requestScope.matriculaProfessor}"></c:out>)</h5>
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
		
		<div class="row">
			<form action="${pageContext.request.contextPath}/analiseAtividades/listarAtividades" method="POST">
          		<table>
	          		<tr>
	          			<td>
	          				<select id="siglaCurso">
	          					<c:forEach items="${requestScope.versoesCursos.keySet()}" var="siglaCurso" >
							  		<option value="${siglaCurso}">${siglaCurso}</option>
							  	</c:forEach>
							</select>
	          			</td>
	          			<td>
	          				<select id="numeroVersao" disabled>
	          					<c:forEach items="${requestScope.versoesCursos.}" var="siglaCurso" >
							  		<option value="${siglaCurso}">${siglaCurso}</option>
							  	</c:forEach>
							</select>
	          			</td>
	          		</tr>
          		</table>
          		
          		<input type="hidden" name="idAtiv" value="${atividade.idAtividade}">
				<button type="submit" class="btn btn-default" title="Registrar nova atividade">
					<i class="fa fa-plus"></i></button>
			</form>
		</div>
		
		<div class="row">
			<h4><b>Registros anteriores:</b></h4>
			<c:choose>
				<c:when test="${fn:length(requestScope.registrosAtiv) eq 0}">
					<div class="vcenter well">
						<p>Não há registros de atividades complementares</p>
					</div>
				</c:when>
				<c:otherwise>
					<div class="vcenter well">
						
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
				</c:otherwise>
			</c:choose>
		</div>
		
		<a class="btn btn-default" href="${pageContext.request.contextPath}/registroAtividades/menuPrincipal">
			<i class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>
</html>
