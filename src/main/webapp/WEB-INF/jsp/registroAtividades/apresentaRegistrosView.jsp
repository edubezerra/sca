<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport"
		content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
		
	<title>SCA - Registro de Atividades Complementares</title>
	
    <link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
		media="screen" rel="stylesheet" type="text/css" />
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/app.js"></script>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/vendor/font-awesome/css/font-awesome.min.css">
</head>
<body class="lista-solicitacoes">

	<div class="container">
		<div class="row text-center">
			<h3>Solicitação de Registro de Atividades Complementares</h3>
		</div>
		<hr/>
		<div class="row">
			<h5><b>Aluno:</b> <c:out value="${requestScope.nomeAluno}"></c:out> (Matrícula: <c:out value="${requestScope.matricula}"></c:out>)</h5>
			<h5><b>Curso:</b> <c:out value="${requestScope.curso.sigla}"></c:out> - <c:out value="${requestScope.curso.nome}"></c:out> (Grade: <c:out value="${requestScope.versaoCurso}"></c:out>)</h5>
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
			<c:if test="${requestScope.temAtividadesSuficientes}">
				<div class="row text-center">
					<span class="label label-success">Parabéns! Você cumpriu atividades complementares suficientes para se formar.</span>
				</div>
			</c:if>
			<c:if test="${!requestScope.temAtividadesSuficientes}">
				<div class="row text-center">
					<span class="label label-warning">
					<i class="fa fa-warning"></i> Você ainda não cumpriu atividades complementares suficientes para se formar.</span>
				</div>
			</c:if>
		</div>
		
		<div class="row">
			<h4><b>Atividades Complementares - Situação atual:</b></h4>
			<div class="well">				
				<table class="table text-center">
					<thead>
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td colspan="3"><b>Carga Horária</b></td>							
							<td></td>
						</tr>
						<tr>
							<td class="text-left"><b>Categoria</b></td>
							<td class="text-left"><b>Descrição</b></td>
							<td><b>Mínima</b></td>
							<td><b>Máxima</b></td>							
							<td><b>Cumprida</b></td>
							<td></td>							
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${requestScope.categorias}" var="categoria" >
							<tr>
					          <td class="text-left"><b>${categoria}</b></td>
					          <td></td>
					          <td></td>
					          <td></td>
					          <td></td>
					          <td></td>
					    	</tr>
							<c:forEach items="${requestScope.situacaoAtiv}" var="atividade" >
								<c:if test="${atividade.categoria eq categoria}">
									<tr>
									  <td></td>
							          <td class="text-left">${atividade.descricao}</td>
							          <td>${atividade.cargaHorariaMin}</td>
							          <td>${atividade.cargaHorariaMax}</td>							          
							          <td>
							          	<c:set var="classeStatus" scope="page">
											<c:choose>
												<c:when test="${atividade.cargaHorariaSuficente}">
													text-success
												</c:when>
												<c:when test="${!atividade.cargaHorariaSuficente}">
													text-danger
												</c:when>
											</c:choose>
										</c:set>
										<p class="${classeStatus}"><b>${atividade.cargaHorariaCumprida}</b></p>
							          </td>
							          <td>
							          	<c:if test="${!atividade.cargaHorariaMaxima}">
											<form action="${pageContext.request.contextPath}/registroAtividades/solicitaRegistroAtividade" method="POST">
								          		<input type="hidden" name="idAtiv" value="${atividade.idAtividade}">
												<button type="submit" class="btn btn-default" title="Registrar nova atividade">
													<i class="fa fa-plus"></i></button>
											</form>
										</c:if>							          	
									  </td>
							        </tr>
						        </c:if>	
							</c:forEach>
						</c:forEach>
						<tr>
							<td colspan="2"></td>
							<td colspan="2"><b>TOTAL:</b></td>							
							<td><b>${requestScope.totalCH}</b></td>
							<td></td>
						</tr>
					</tbody>
				</table>
			</div>
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
									<td><b>Categoria da Atividade</b></td>
									<td><b>Descrição</b></td>
									<td><b>Carga Horária</b></td>
									<td><b>Comprovante</b></td>
									<td><b>Data de Solicitação</b></td>
									<td><b>Status</b></td>
																	
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${requestScope.registrosAtiv}" var="registro" >
									<tr>
							          <td>${registro.categoria}</td>
							          <td>${registro.descricao}</td>
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
													text-primary
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