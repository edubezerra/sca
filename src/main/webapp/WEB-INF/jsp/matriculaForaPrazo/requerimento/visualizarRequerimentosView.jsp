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
			<hr />
			<div class="row">
				<span class="label label-success">${requestScope.sucesso}</span>
			</div>
			<hr />
		</c:if>

		<c:if test="${requestScope.error != null}">
			<hr />
			<div class="row">
				<span class="label label-danger">${requestScope.error}</span>
			</div>
			<hr />
		</c:if>

		<div class="row">
			<c:choose>
				<c:when test="${fn:length(requestScope.listaPeriodosLetivos) eq 0}">
					<div class="vcenter well ">
						<p>Não há requerimentos anteriores.</p>
					</div>
				</c:when>
				<c:otherwise>

					<div class="panel panel-primary">
						<div class="panel-heading">
							<h3 class="panel-title">Requerimentos por Período Letivo</h3>
						</div>
						<div class="panel-body">
							<table class="table table-bordered table-hover table-striped">
								<tbody>
									<c:forEach items="${listaPeriodosLetivos}" var="periodoLetivo">
										<tr>
											<td>
												<form
													action="${pageContext.request.contextPath}/matriculaForaPrazo/requerimento/visualizarDetalhesRequerimento"
													method="POST">
													<input type="hidden" name="ano"
														value="${periodoLetivo.ano}"> <input type="hidden"
														name="periodo" value="${periodoLetivo.periodo}">
													<button type="submit" class="btn btn-link">${periodoLetivo}</button>
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
			<div class="row">
				<form
					action="${pageContext.request.contextPath}/matriculaForaPrazo/requerimento/iniciarRegistroRequerimento"
					method="POST">

					<c:if test="${requestScope.periodoLetivoCorrente == null}">
						<button type="submit" class="btn btn-primary">Criar
							Requerimento</button>
					</c:if>
					<c:if test="${requestScope.periodoLetivoCorrente != null}">
						<button type="submit" class="btn btn-primary">Editar
							Requerimento (${requestScope.periodoLetivoCorrente})</button>
					</c:if>

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
