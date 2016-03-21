<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>
<meta charset="UTF-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
<title>SCA</title>

<link href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/app.js"></script>

</head>

<body class="home-inclusao text-center">
	<div class="container">
		<div class="row">
			<h2>Solicitação de Matrícula Fora do Prazo</h2>
			<h4>
				Aluno:
				<c:out value="${requestScope.aluno.nome}"></c:out>
				(Matrícula:
				<c:out value="${requestScope.aluno.matricula}"></c:out>)
			</h4>
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
						<p>Não há solicitações anteriores</p>
					</div>
				</c:when>
				<c:otherwise>
					<div class="table-responsive">
						<h4>Solicitações anteriores:</h4>
						<table class="table table-bordered text-center">
							<thead>
								<tr>
									<th class="text-center">Período Letivo</th>
									<th></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${listaSemestresLetivos}" var="semestreLetivo">
									<tr>
										<td>${semestreLetivo}</td>
										<td>
											<form
												action="${pageContext.request.contextPath}/inclusaoDisciplina/listarSolicitacoes"
												method="POST">
												<input type="hidden" name="ano"
													value="${semestreLetivo.ano}"> <input type="hidden"
													name="periodo" value="${semestreLetivo.periodo}">
												<button type="submit" class="btn btn-link">Detalhes</button>
											</form>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
				</c:otherwise>
			</c:choose>
			<div class="row">
				<c:choose>
					<c:when test="${numeroSolicitacoes lt 3}">
						<form
							action="${pageContext.request.contextPath}/inclusaoDisciplina/solicitaInclusaoDisciplinas"
							method="POST">
							<input type="hidden" name="numeroSolicitacoes"
								value="${numeroSolicitacoes}">
							<button type="submit" class="btn btn-primary">Fazer
								Solicitação</button>
						</form>
					</c:when>
					<c:otherwise>
						<span class="label label-warning">Você já fez o numero
							máximo de solicitações para o período atual.</span>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>

</html>
