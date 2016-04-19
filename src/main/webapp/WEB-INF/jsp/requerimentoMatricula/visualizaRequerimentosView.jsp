<%@ page language="java" pageEncoding="UTF-8"
	contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SCA</title>

<link
	href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.css"
	media="screen" rel="stylesheet" type="text/css" />
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/jquery/jquery-1.10.2.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/bootstrap/js/bootstrap.js"></script>
<script type="text/javascript"
	src="${pageContext.request.contextPath}/resources/js/app.js"></script>

</head>

<body class="text-center">
	<div class="container">
		<div class="row">
			<h2>Requerimentos de Matrícula Fora do Prazo</h2>
			<h4>Aluno: ${requestScope.aluno.nome} (Matrícula
				${requestScope.aluno.matricula})</h4>
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
						<p>Não há requerimentos anteriores.</p>
					</div>
				</c:when>
				<c:otherwise>

					<table class="table table-bordered table-hover table-striped">
						<thead>
							<tr>
								<th>Requerimentos por Período Letivo</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${listaSemestresLetivos}" var="periodoLetivo">
								<tr>
									<td>
										<form
											action="${pageContext.request.contextPath}/requerimentoMatricula/visualizarDetalhesRequerimento"
											method="POST">
											<input type="hidden" name="ano" value="${periodoLetivo.ano}">
											<input type="hidden" name="periodo"
												value="${periodoLetivo.periodo}">
											<button type="submit" class="btn btn-link">${periodoLetivo}</button>
										</form>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>

				</c:otherwise>
			</c:choose>
			<div class="row">
				<%-- 				<c:choose> --%>
				<%-- 					<c:when test="${numeroSolicitacoes lt 3}"> --%>
				<form
					action="${pageContext.request.contextPath}/requerimentoMatricula/solicitaInclusaoDisciplinas"
					method="POST">
					<button type="submit" class="btn btn-primary">Realizar
						Novo Requerimento</button>
				</form>
			</div>
		</div>
		<a class="btn btn-default"
			href="${pageContext.request.contextPath}/menuPrincipalView"> <i
			class="fa fa-arrow-left"> </i> Voltar
		</a>
	</div>

</body>

</html>
