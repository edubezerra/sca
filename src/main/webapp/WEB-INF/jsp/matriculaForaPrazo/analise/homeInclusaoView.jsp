<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>SCA - Análise de Matrículas Fora do Prazo</title>

<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>

<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>

</head>

<body class="home-inclusao text-center">
	<div class="container">
		<div class="row">
			<h2>Análise de Matrículas Fora do Prazo</h2>
			<h4>Analisador: ${requestScope.professor.nome} (matrícula
				${requestScope.professor.matricula} )</h4>
		</div>
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
		<div class="row">
			<c:choose>
				<c:when test="${fn:length(requestScope.listaSemestresLetivos) eq 0}">
					<div class="vcenter well ">
						<p>Não há requerimentos para analisar.</p>
					</div>
				</c:when>
				<c:otherwise>

					<div class="panel panel-primary">
						<div class="panel-heading">
							<h3 class="panel-title">Requerimento</h3>
						</div>
						<div class="panel-body">
							<table class="table table-bordered text-center">
								<thead>
									<tr>
										<th class="text-center">Período Letivo</th>
										<th></th>
									</tr>
								</thead>
								<tbody>
									<c:forEach items="${listaSemestresLetivos}"
										var="semestreLetivo">
										<tr>
											<td>${semestreLetivo}</td>
											<td>
												<form
													action="${pageContext.request.contextPath}/matriculaForaPrazo/analise/listarSolicitacoes"
													method="POST">
													<input type="hidden" name="ano"
														value="${semestreLetivo.ano}"> <input
														type="hidden" name="periodo"
														value="${semestreLetivo.periodo}">
													<button type="submit" class="btn btn-link">Ver
														Requerimentos</button>
												</form>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>

</html>
